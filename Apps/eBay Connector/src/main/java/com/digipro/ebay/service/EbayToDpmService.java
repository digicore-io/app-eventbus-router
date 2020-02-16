package com.digipro.ebay.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

		EntityApiResponse response = null;

		System.err.println("Processing Event from Ebay. Event: " + event.getEvent() + "\n\nPayload:\n" + GsonUtil.gson.toJson(event.getPayload()));
		try {
			CompanyAppDao companyAppDao = new CompanyAppDao();
			CompanyApp companyApp = companyAppDao.load(event.getCompanyId(), event.getApplicationId());

			if (companyApp == null)
				throw new RuntimeException("Could not retrieve companyApp. This needs to be present");

			CategoryConfig catConfig = getCategoryFilter(companyApp);

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
			Product product = prodService.getProductFromItemXML(event.getPayload().getAsString(), event.getCompanyId(), defaultFamilyId, schema, dao, catConfig);

			System.err.println("XML to JSON from eBay: " + GsonUtil.gson.toJson(product));
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
					throw new Exception(String.format("Could not save entity so won't be able to update DPM product on ebay update. Company ID %s - Product ID %s - Ebay Item ID %s", event.getCompanyId(), productId,
							product.getEbayItemId()));

				logToSlack("devops-ebay-app", CoreService.STAGE.toUpperCase(), "Ebay Connector",
						String.format("INSERTED product in DPM from eBay:\n\nCompany ID: %s\nDPM Product ID: %s\neBay Item ID: %s\nTitle: %s\neBay Qty: %s\nDB Schema: %s", event.getCompanyId(), product.getProductId(),
								product.getEbayItemId(), StringUtils.trim(product.getTitle()), product.getQuantity(), GsonUtil.gson.fromJson(companyAppEvent.getConfig(), JsonObject.class).get("schema").getAsString()));

			} else {
				if (!String.valueOf(code).startsWith("2"))
					throw new Exception("Invalid response from App Manager - Response Code: " + code);

				response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				product.setProductId(response.getPayload().getData().getProductId());
				dao.updateProduct(product, schema, false);
				dao.updateProductData(product, schema, true);
				logToSlack("devops-ebay-app", CoreService.STAGE.toUpperCase(), "Ebay Connector",
						String.format("UPDATED product in DPM from eBay:\n\nCompany ID: %s\nDPM Product ID: %s\neBay Item ID: %s\nTitle: %s\neBay Qty: %s\nDB Schema: %s", event.getCompanyId(), product.getProductId(),
								product.getEbayItemId(), StringUtils.trim(product.getTitle()), product.getQuantity(), GsonUtil.gson.fromJson(companyAppEvent.getConfig(), JsonObject.class).get("schema").getAsString()));
			}
		} catch (Exception e) {

			String message = null;
			if (response != null)
				message = String.format("Exception listing/updating a product on eBay. Company ID: %s - eBay Item ID: %s", event.getCompanyId(), response.getPayload().getData().getItemId());
			else
				message = String.format("Exception listing/updating a product  on eBay. Company ID: %s - Payload %s", event.getCompanyId(), e.getMessage() + "\n\n" + event.getPayload().getAsString());

			message += "\n\n" + ExceptionUtils.getStackTrace(e);

			logToSlack(BaseService.DEV_OPS_SLACK_CHANNEL, CoreService.STAGE, "App - eBayConnector", message);
			logError(event, e);
			throw new RuntimeException(e);
		}
	}

	public CategoryConfig getCategoryFilter(CompanyApp companyApp) {
		if (companyApp.getConfig() == null)
			return null;

		CategoryConfig config = new CategoryConfig();
		JsonObject filter = GsonUtil.gson.fromJson(companyApp.getConfig(), JsonObject.class);
		if (filter.get("categoryFilter") == null)
			return null;

		if (filter.get("categoryLevel") != null)
			config.setCategoryLevel(filter.get("categoryLevel").getAsInt());

		Set<String> catFilter = new HashSet<>();
		JsonArray array = filter.get("categoryFilter").getAsJsonArray();
		for (int i = 0; i < array.size(); i++)
			catFilter.add(array.get(i).getAsString());

		config.setCategoryFilter(catFilter);

		return config;
	}

}
