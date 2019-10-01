package com.digipro.ebay.service;

import java.util.Calendar;
import java.util.Properties;

import org.apache.http.HttpStatus;

import com.digipro.ebay.dao.ProductDao;
import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;

import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.dao.CompanyAppEventDao;
import io.digicore.lambda.model.CompanyAppEvent;
import io.digicore.lambda.ro.CompanyEventRo;

public class EbayToDpmService {

	Properties props;

	public EbayToDpmService(Properties props) {
		this.props = props;
	}

	public void processEbayProductChange(CompanyEventRo event, String apiKey) {

		try {
			CompanyAppEventDao eventDao = new CompanyAppEventDao();
			CompanyAppEvent appEvent = eventDao.find(event.getCompanyId(), event.getEvent());
			if (appEvent == null)
				throw new RuntimeException("Could not retrieve companyAppEvent. This needs to be present with db schema and productFamilyId in config");

			JsonObject config = GsonUtil.gson.fromJson(appEvent.getConfig(), JsonObject.class);
			String schema = config.get("schema").getAsString();
			if (schema == null)
				throw new RuntimeException("Could not schema");

			if (config.get("defaultFamilyId") == null)
				throw new RuntimeException("Could not get defaultFamilyId");

			ProductDao dao = new ProductDao();
			String defaultFamilyId = config.get("defaultFamilyId").getAsString();
			ProductService prodService = new ProductService();
			Product product = prodService.getProductFromItemXML(event.getPayload().getAsString(), event.getCompanyId(), defaultFamilyId, schema, dao);

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
			} else {
				if (!String.valueOf(code).startsWith("2"))
					throw new Exception("Invalid response from App Manager");

				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				product.setProductId(response.getPayload().getData().getProductId());
				dao.updateProduct(product, schema, true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
