package com.digipro.ebay.service;

import java.util.Calendar;
import java.util.Properties;

import org.apache.http.HttpStatus;

import com.digipro.ebay.dao.CompanyAppEventDao;
import com.digipro.ebay.dao.ProductDao;
import com.digipro.ebay.model.CompanyAppEvent;
import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.Event;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;

public class EbayToDpmService {

	Properties props;

	public EbayToDpmService(Properties props) {
		this.props = props;
	}

	public void processEbayProductChange(Event event, String apiKey) {

		try {
			CompanyAppEventDao eventDao = new CompanyAppEventDao();
			CompanyAppEvent appEvent = eventDao.find(event.getCompanyId(), event.getEvent());
			if (appEvent == null)
				throw new RuntimeException("Could not retrieve companyAppEvent. This needs to be present with db schema and productFamilyId in config");

			JsonObject config = GsonUtil.gson.fromJson(appEvent.getConfig(), JsonObject.class);
			String schema = config.get("schema").getAsString();
			if (schema == null)
				throw new RuntimeException("Could not schema");

			XmlMapper xmlMapper = new XmlMapper();
			String xml = event.getPayload();
			xml = xml.substring(xml.indexOf("<soapenv:Body>") + 14, xml.length());
			xml = xml.substring(0, xml.indexOf("</soapenv:Body>"));

			JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
			Product product = getProduct(event, jsonNode, config);

			//Check if this is an insert or update
			String endpoint = String.format("applications/%s/companies/%s/entities/%s", event.getApplicationId(), event.getCompanyId(), product.getEbayItemId());
			HttpRequest request = HttpRequest.get(props.getProperty("APP_MANAGER_URL") + endpoint).header("x-api-key", apiKey);
			int code = request.code();

			ProductDao dao = new ProductDao();
			if (code == HttpStatus.SC_NOT_FOUND) {
				String productId = dao.insertProduct(product, schema, true);
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
				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				product.setProductId(response.getPayload().getData().getProductId());
				dao.updateProduct(product, schema, true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Product getProduct(Event event, JsonNode jsonNode, JsonObject config) {

		if (config.get("productFamilyId") == null)
			throw new RuntimeException("Could not get productFamilyId");

		JsonNode item = jsonNode.get("Item");
		String strRegEx = "<[^>]*>";
		Product product = new Product();
		product.setEbayItemId(item.get("ItemID").textValue());
		product.setProductFamilyId(config.get("productFamilyId").getAsString());
		product.setTitle(item.get("Title").textValue());
		product.setDescription(item.get("Description").textValue().replaceAll(strRegEx, ""));
		product.setSlug(product.getTitle().replace(" ", "-"));
		product.setPrice(item.get("SellingStatus").get("CurrentPrice").get("").textValue());
		product.setQuantity(item.get("Quantity").textValue());
		product.setLocId(event.getCompanyId());
		product.setPrimaryImageAlt(product.getTitle());
		product.setCreated(Calendar.getInstance());

		if (item.get("PictureDetails").get("PictureURL") != null)
			product.setPrimaryImage(item.get("PictureDetails").get("PictureURL").textValue());

		if (item.get("Quantity").asInt() > 0)
			product.setStatus(1);
		else
			product.setStatus(0);

		return product;
	}
}
