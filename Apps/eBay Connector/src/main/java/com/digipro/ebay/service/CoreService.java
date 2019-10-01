package com.digipro.ebay.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.apache.http.HttpStatus;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import com.digipro.ebay.dao.EventLogDao;
import com.digipro.ebay.model.EventLog;
import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.DpmPayload;
import com.digipro.ebay.ro.Event;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.ebay.sdk.ApiException;
import com.github.kevinsawicki.http.HttpRequest;

/**
 * https://github.com/kevinsawicki/http-request
 * 
 *
 */
public class CoreService {
	private static String ENV = "local";
	private static Properties props;
	private static String apiKey;

	public CoreService() {
		if (System.getenv("STAGE") != null)
			ENV = System.getenv("STAGE");

		System.err.println("Environment " + ENV);

		if (apiKey == null)
			apiKey = ParamUtils.getParameter("api-key-main");

		if (props == null) {
			try {
				props = new Properties();
				props.load(getClass().getClassLoader().getResourceAsStream(ENV + ".properties"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private String getApiKey() {
		try {
			AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();

			GetParameterRequest pr = new GetParameterRequest();
			pr.withName("api-key-main").setWithDecryption(true);
			GetParameterResult result = client.getParameter(pr);
			Parameter param = result.getParameter();

			return param.getValue();
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Couldn't get the API key from SSM");
		}
	}

	public void processMessage(Event event) {
		if (event.getEvent().equals(com.digipro.ebay.service.Event.EXTERNAL_EBAY_PRODUCT_CHANGE.name())) {
			EbayToDpmService service = new EbayToDpmService(props);
			service.processEbayProductChange(event, getApiKey());
		} else
			processDpmMessage(event);
	}

	public void processDpmMessage(Event event) {
		String apiKey = getApiKey();
		System.err.println("Processing Message");

		DpmPayload payload = GsonUtil.gson.fromJson((String) event.getPayload(), DpmPayload.class);
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

			} else if (code == HttpStatus.SC_OK) {
				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				service.updateProductListing(response.getPayload().getData().getItemId(), event, payload);
			} else
				throw new Exception("Unexpected response code from Api App Manager: " + code);

		} catch (ApiException ae) {
			//TODO: Problem with the listing. Send notification to App Slack Channel

			System.err.println(ae.getMessage());
			log(event, ae.getMessage(), LogStatus.ERROR);
			throw new RuntimeException(ae);
		} catch (Exception e) {
			//TODO:  Send notification to EventBus Slack Channel

			e.printStackTrace();
			logError(event, e);
			throw new RuntimeException(e);
		}
	}

	public void log(Event appEvent, Object info, LogStatus status) {
		EventLog log = new EventLog();
		log.setId(UUID.randomUUID().toString());
		log.setApplicationId(appEvent.getApplicationId());
		log.setCreated(Calendar.getInstance());
		log.setStatus(status.name());
		log.setEventRequest(GsonUtil.gson.toJson(appEvent));

		if (info instanceof String)
			log.setInfo((String) info);
		else
			log.setInfo(GsonUtil.gson.toString());

		EventLogDao dao = new EventLogDao();
		dao.save(log);
	}

	public void logError(Event appEvent, Exception e) {
		EventLog log = new EventLog();
		log.setId(UUID.randomUUID().toString());
		log.setApplicationId(appEvent.getApplicationId());
		log.setCreated(Calendar.getInstance());
		log.setStatus(LogStatus.EXCEPTION.name());
		log.setEventRequest(GsonUtil.gson.toJson(appEvent));

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.setInfo(sw.toString());

		EventLogDao dao = new EventLogDao();
		dao.save(log);
	}
}
