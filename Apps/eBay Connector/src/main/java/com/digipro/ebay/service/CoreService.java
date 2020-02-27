package com.digipro.ebay.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;

import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.DpmPayload;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.ebay.sdk.ApiException;
import com.github.kevinsawicki.http.HttpRequest;

import io.digicore.lambda.BaseService;
import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.dao.CompanyAppDao;
import io.digicore.lambda.model.CompanyApp;
import io.digicore.lambda.model.LogStatus;
import io.digicore.lambda.ro.CompanyEventRo;

/**
 * https://github.com/kevinsawicki/http-request
 * 
 *
 */
public class CoreService extends BaseService {
	public static String STAGE = "local";
	private static Properties props;
	private static String apiKey;

	public CoreService() {
		if (System.getenv("STAGE") != null)
			STAGE = System.getenv("STAGE");
		else
			throw new RuntimeException("STAGE must be set in your environment variables");

		if (apiKey == null)
			apiKey = getParameter("digicore-api-key");

		if (props == null) {
			try {
				props = new Properties();
				props.load(getClass().getClassLoader().getResourceAsStream(STAGE + ".properties"));
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
			CompanyAppDao dao = new CompanyAppDao();
			System.err.println(GsonUtil.gson.toJson(event));
			CompanyApp companyApp = dao.load(event.getCompanyId(), event.getApplicationId());

			DpmToEbayService service = new DpmToEbayService(props);
			if (code == HttpStatus.SC_NOT_FOUND) {
				String itemId = service.createProductListing(event, payload, companyApp);

				if (itemId != null) { //Product not created due to status=0

					AppEntity item = new AppEntity();
					item.setCompanyId(event.getCompanyId());
					item.setDigicoreEntityId(payload.getId());
					item.setExternalEntityId(itemId);

					String responseCode = "" + HttpRequest.put(props.getProperty("APP_MANAGER_URL") + endpoint).header("x-api-key", apiKey).send(GsonUtil.gson.toJson(item)).code();

					if (!responseCode.startsWith("2"))
						throw new Exception(String.format("Could not save response from eBay. Company ID %s - Product ID %s - Ebay Item ID %s", event.getCompanyId(), payload.getId(), itemId));
				}

				return itemId;

			} else if (code == HttpStatus.SC_OK) {
				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				service.updateStockOnHand(response.getPayload().getExternalEntityId(), event, payload, companyApp);
				return response.getPayload().getExternalEntityId();
			} else
				throw new Exception("Unexpected response code from Api App Manager: " + code);

		} catch (ApiException ae) {
			//TODO: Problem with the listing. Send notification to App Slack Channel
			String message = String.format("Problem listing/updating a product on eBay. Company ID: $s - Product ID: $s", companyId, entityId);
			message += "\n\n" + ae.getMessage();
			System.err.println(message);

			logToSlack("devops-ebay-app", CoreService.STAGE, "App - eBay Connector", message);
			log(event, message, LogStatus.ERROR);
			throw new RuntimeException(ae);
		} catch (Exception e) {

			String message = String.format("Exception listing/updating a product on eBay. Company ID: $s - Product ID: $s", companyId, entityId);
			message += "\n\n" + ExceptionUtils.getStackTrace(e);

			logToSlack(BaseService.DEV_OPS_SLACK_CHANNEL, CoreService.STAGE, "App - eBayConnector", message);
			logError(event, e);
			throw new RuntimeException(e);
		}
	}

}
