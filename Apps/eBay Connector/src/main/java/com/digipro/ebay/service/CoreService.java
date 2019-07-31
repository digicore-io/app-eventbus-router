package com.digipro.ebay.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.http.HttpStatus;

import com.digipro.ebay.dao.EventLogDao;
import com.digipro.ebay.model.EventLog;
import com.digipro.ebay.ro.AppEntity;
import com.digipro.ebay.ro.Body;
import com.digipro.ebay.ro.Record;
import com.digipro.ebay.ro.api.EntityApiResponse;
import com.ebay.sdk.ApiException;
import com.github.kevinsawicki.http.HttpRequest;

/**
 * https://github.com/kevinsawicki/http-request
 * 
 *
 */
public class CoreService {
	//TODO: Replace this to work with stage param (defaulting to local)
	private static final String ENV = "local";
	private Properties props;

	public CoreService() {
		if (props == null) {
			try {
				props = new Properties();
				props.load(getClass().getClassLoader().getResourceAsStream(ENV + ".properties"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void processMessage(Record record) {
		//Check if we've processed this before
		String appId = record.getBody().getApplicationId();
		String companyId = record.getBody().getCompanyId();
		String entityId = record.getBody().getPayload().getId();

		String endpoint = String.format("applications/%s/companies/%s/entities/%s", appId, companyId, entityId);
		HttpRequest request = HttpRequest.get(props.getProperty("APP_MANAGER_URL") + endpoint);
		int code = request.code();

		try {
			EbayService service = new EbayService(props);
			if (code == HttpStatus.SC_NOT_FOUND) {
				String itemId = service.createProductListing(record.getBody());

				if (itemId != null) { //Product not created due to status=0

					AppEntity item = new AppEntity();
					item.setCompanyId(record.getBody().getCompanyId());
					item.setEntityId(record.getBody().getPayload().getId());
					item.getData().setItemId(itemId);

					String responseCode = "" + HttpRequest.put(props.getProperty("APP_MANAGER_URL") + endpoint).send(GsonUtil.gson.toJson(item)).code();

					if (!responseCode.startsWith("2"))
						throw new Exception(
								String.format("Could not save response from eBay. Company ID %s - Product ID %s - Ebay Item ID %s", record.getBody().getCompanyId(), record.getBody().getPayload().getId(), itemId));
				}

			} else {
				EntityApiResponse response = GsonUtil.gson.fromJson(request.body(), EntityApiResponse.class);
				service.updateProductListing(response.getPayload().getData().getItemId(), record.getBody());
			}

		} catch (ApiException ae) {
			//TODO: Problem with the listing. Send notification to App Slack Channel

			System.err.println(ae.getMessage());
			log(record.getBody(), ae.getMessage(), LogStatus.ERROR);
			throw new RuntimeException(ae);
		} catch (Exception e) {
			//TODO:  Send notification to EventBus Slack Channel

			e.printStackTrace();
			logError(record.getBody(), e);
			throw new RuntimeException(e);
		}
	}

	public void log(Body appEvent, Object info, LogStatus status) {
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

	public void logError(Body appEvent, Exception e) {
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
