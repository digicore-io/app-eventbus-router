package com.serverless;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.util.IOUtils;
import com.digipro.ebay.service.GsonUtil;

/**
 * TODO: Ensure we have 100% test coverage and cleanup 
 * i.e. remove listing from ebay so it can be re added
 */
class HandlerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testProcessSQSNewProductFromDPM() {
		try {
			Handler handler = new Handler();
			String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs-from-dpm.json"));
			SQSEvent event = new SQSEvent();
			event.setRecords(new ArrayList<SQSEvent.SQSMessage>());
			SQSMessage msg = new SQSMessage();
			msg.setBody(json);
			event.getRecords().add(msg);

			handler.handleRequest(event, null);

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testProcessNewMessageFromEbay() {
		try {
			Handler handler = new Handler();
			String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs-from-ebay.json"));
			Event eventRo = GsonUtil.gson.fromJson(json, Event.class);
			eventRo.setPayload(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("product-listed-ebay.xml")));

			json = GsonUtil.gson.toJson(eventRo);

			SQSEvent event = new SQSEvent();
			event.setRecords(new ArrayList<SQSEvent.SQSMessage>());
			SQSMessage msg = new SQSMessage();
			msg.setBody(json);
			event.getRecords().add(msg);

			handler.handleRequest(event, null);

		} catch (Exception e) {
			fail(e);
			e.printStackTrace();
		}
	}
}
