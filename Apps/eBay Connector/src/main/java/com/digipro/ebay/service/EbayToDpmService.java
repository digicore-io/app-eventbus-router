package com.digipro.ebay.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpStatus;

import com.digipro.ebay.dao.ProductDao;
import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.digicore.lambda.BaseService;
import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.dao.CompanyAppDao;
import io.digicore.lambda.dao.CompanyAppEventDao;
import io.digicore.lambda.model.CompanyApp;
import io.digicore.lambda.model.CompanyAppEvent;
import io.digicore.lambda.ro.CompanyEventRo;

public class EbayToDpmService extends BaseService {

	Properties props;

	public EbayToDpmService(Properties props) {
		this.props = props;
	}

	public void processEbayProductChange(CompanyEventRo event, String apiKey) {

		try {
			CompanyAppDao companyAppDao = new CompanyAppDao();
			CompanyApp companyApp = companyAppDao.load(event.getCompanyId(), event.getApplicationId());

			if (companyApp == null)
				throw new RuntimeException("Could not retrieve companyApp. This needs to be present");

			Set<String> categoryFilter = getCategoryFilter(companyApp);

			CompanyAppEventDao companyAppEventDao = new CompanyAppEventDao();
			CompanyAppEvent companyAppEvent = companyAppEventDao.find(event.getCompanyId(), event.getEvent());
			if (companyAppEvent == null)
				throw new RuntimeException("Could not retrieve companyAppEvent. This needs to be present with db schema and productFamilyId in config");

			JsonObject config = GsonUtil.gson.fromJson(companyAppEvent.getConfig(), JsonObject.class);
			String schema = config.get("schema").getAsString();
			if (schema == null)
				throw new RuntimeException("Could not schema");

			if (config.get("defaultFamilyId") == null)
				throw new RuntimeException("Could not get defaultFamilyId");

			ProductDao dao = new ProductDao();
			String defaultFamilyId = config.get("defaultFamilyId").getAsString();
			ProductService prodService = new ProductService();
			Product product = prodService.getProductFromItemXML(event.getPayload().getAsString(), event.getCompanyId(), defaultFamilyId, schema, dao, categoryFilter);

			//Check if this is an insert or update
			String endpoint = String.format("applications/%s/companies/%s/entities/%s", event.getApplicationId(), event.getCompanyId(), product.getEbayItemId());
			HttpRequest request = HttpRequest.get(props.getProperty("APP_MANAGER_URL") + endpoint).header("x-api-key", apiKey);
			int code = request.code();

			if (code == HttpStatus.SC_NOT_FOUND) {
				String productId = dao.insertProduct(product, schema, false);
				dao.insertProductData(product, schema, true);

				AppEntity entity = new AppEntity();
				entity.setCompanyId(event.getCompanyId());
				entity.setEntityId(product.getEbayItemId());
				entity.getData().setProductId(productId);

				String responseCode = "" + HttpRequest.put(props.getProperty("APP_MANAGER_URL") + endpoint).header("x-api-key", apiKey).send(GsonUtil.gson.toJson(entity)).code();

				if (!responseCode.startsWith("2"))
					throw new Exception(
							String.format("Could not save entity so won't be able to update DPM product on ebay update. Company ID %s - Product ID %s - Ebay Item ID %s",
									event.getCompanyId(), productId, product.getEbayItemId()));

				logToSlack("devops-ebay-app", "Ebay Connector", "Inserted new product in DPM from eBay. Product ID: " + productId);

			} else {
				if (!String.valueOf(code).startsWith("2"))
					throw new Exception("Invalid response from App Manager");

				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				product.setProductId(response.getPayload().getData().getProductId());
				dao.updateProduct(product, schema, true);

				logToSlack("devops-ebay-app", "Ebay Connector", "Updated product in DPM from eBay. Product ID: " + response.getPayload().getData().getProductId());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Set<String> getCategoryFilter(CompanyApp companyApp) {
		//Set<String> catFilter = new HashSet<>();
		//		catFilter.add("Healthcare, Lab & Dental");
		//		catFilter.add("Medical & Mobility");
		//		catFilter.add("Health Care");
		//		return catFilter;
		if (companyApp.getConfig() == null)
			return null;

		JsonObject filter = GsonUtil.gson.fromJson(companyApp.getConfig(), JsonObject.class);
		if (filter.get("categoryFilter") == null)
			return null;

		Set<String> catFilter = new HashSet<>();
		JsonArray array = filter.get("categoryFilter").getAsJsonArray();
		for (int i = 0; i < array.size(); i++)
			catFilter.add(array.get(i).getAsString());

		return catFilter;
	}

}
