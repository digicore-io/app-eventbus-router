package com.serverless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.digipro.ebay.service.CoreService;

import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.ro.CompanyEventRo;

public class Handler implements RequestHandler<SQSEvent, String> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public String handleRequest(SQSEvent sqsEvent, Context context) {
		try {
			//			ProductDao dao = new ProductDao();
			//			dao.selectTest();

			CoreService service = new CoreService();
			String itemId = null;
			for (SQSMessage msg : sqsEvent.getRecords()) {
				System.err.println("Request received:\n\n");
				System.err.println(GsonUtil.gson.toJson(msg));
				System.err.println("\n\n--------------------");
				CompanyEventRo event = GsonUtil.gson.fromJson(msg.getBody(), CompanyEventRo.class);
				itemId = service.processMessage(event);
			}
			return itemId;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
			//Don't throw this unless you want retries
			//throw new RuntimeException(e);
		}

	}

}
