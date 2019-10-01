package com.digipro.ebay.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.http.HttpStatus;

import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.DpmPayload;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.ebay.sdk.ApiException;
import com.github.kevinsawicki.http.HttpRequest;

import io.digicore.lambda.BaseService;
import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.model.LogStatus;
import io.digicore.lambda.ro.CompanyEventRo;

/**
 * https://github.com/kevinsawicki/http-request
 * 
 *
 */
public class CoreService extends BaseService {
	private static String ENV = "local";
	private static Properties props;
	private static String apiKey;

	public CoreService() {
		if (System.getenv("STAGE") != null)
			ENV = System.getenv("STAGE");
		else
			throw new RuntimeException("STAGE must be set in your environment variables");

		if (apiKey == null)
			apiKey = getParameter("api-key-main");

		if (props == null) {
			try {
				props = new Properties();
				props.load(getClass().getClassLoader().getResourceAsStream(ENV + ".properties"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String processMessage(CompanyEventRo event) {
		if (event.getEvent().equals(com.digipro.ebay.service.Event.EXTERNAL_EBAY_PRODUCT_CHANGE.name())) {
			EbayToDpmService service = new EbayToDpmService(props);
			service.processEbayProductChange(event, apiKey);
			return null;
		} else
			return processDpmMessage(event);
	}

	public String processDpmMessage(CompanyEventRo event) {
		System.err.println("Processing Message");

		DpmPayload payload = GsonUtil.gson.fromJson(event.getPayload().toString(), DpmPayload.class);
		String appId = event.getApplicationId();
		String companyId = event.getCompanyId();
		String entityId = payload.getId();

		//Check if we've processed this before
		String endpoint = String.format("applications/%s/companies/%s/entities/%s", appId, companyId, entityId);
		HttpRequest request = HttpRequest.get(props.getProperty("APP_MANAGER_URL") + endpoint).header("x-api-key", apiKey);
		int code = request.code();

		try {
			DpmToEbayService service = new DpmToEbayService(props);
			if (code == HttpStatus.SC_NOT_FOUND) {
				String itemId = service.createProductListing(event, payload);

				if (itemId != null) { //Product not created due to status=0

					AppEntity item = new AppEntity();
					item.setCompanyId(event.getCompanyId());
					item.setEntityId(payload.getId());
					item.getData().setItemId(itemId);

					String responseCode = "" + HttpRequest.put(props.getProperty("APP_MANAGER_URL") + endpoint).header("x-api-key", apiKey).send(GsonUtil.gson.toJson(item)).code();

					if (!responseCode.startsWith("2"))
						throw new Exception(
								String.format("Could not save response from eBay. Company ID %s - Product ID %s - Ebay Item ID %s", event.getCompanyId(), payload.getId(), itemId));
				}

				return itemId;

			} else if (code == HttpStatus.SC_OK) {
				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				service.updateProductListing(response.getPayload().getData().getItemId(), event, payload);
				return response.getPayload().getData().getItemId();
			} else
				throw new Exception("Unexpected response code from Api App Manager: " + code);

		} catch (ApiException ae) {
			//TODO: Problem with the listing. Send notification to App Slack Channel
			String message = String.format("Problem listing/updating a product on eBay. Company ID: $s - Product ID: $s", companyId, entityId);
			message += "\n\n" + ae.getMessage();
			System.err.println(message);

			logToSlack("devops-ebay-app", "App - eBay Connector", message);
			log(event, message, LogStatus.ERROR);
			throw new RuntimeException(ae);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			String message = String.format("Exception listing/updating a product on eBay. Company ID: $s - Product ID: $s", companyId, entityId);
			message += "\n\n" + sw.toString();

			logToSlack("dev-ops-eventbus", "App - eBayConnector", message);
			logError(event, e);
			throw new RuntimeException(e);
		}
	}

}
