package com.serverless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.digipro.ebay.ro.Event;
import com.digipro.ebay.service.CoreService;
import com.digipro.ebay.service.GsonUtil;

public class Handler implements RequestHandler<SQSEvent, Void> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public Void handleRequest(SQSEvent sqsEvent, Context context) {
		try {
			CoreService service = new CoreService();
			for (SQSMessage msg : sqsEvent.getRecords()) {
				Event event = GsonUtil.gson.fromJson(msg.getBody(), Event.class);
				service.processMessage(event);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//TODO: implement endpoint in router for API gateway for eBay notifications

}
