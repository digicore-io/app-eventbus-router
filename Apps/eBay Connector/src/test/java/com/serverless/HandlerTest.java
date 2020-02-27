package com.serverless;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.util.IOUtils;
import com.google.gson.JsonObject;

import io.digicore.lambda.GsonUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HandlerTest {

	//So we can run update independently from update
	boolean testProcessSQSNewProductFromDPMHasRun = false;

	String dpmJsonProduct;

	@BeforeEach
	void setUp() throws Exception {
		dpmJsonProduct = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs-from-dpm.json"));
		io.digicore.lambda.ro.CompanyEventRo event = GsonUtil.gson.fromJson(dpmJsonProduct, io.digicore.lambda.ro.CompanyEventRo.class);
		long time = Calendar.getInstance().getTimeInMillis();

		JsonObject jsonObject = event.getPayload().getAsJsonObject();
		jsonObject.addProperty("id", String.valueOf(time));
		jsonObject.addProperty("name", "Test " + time);

		event.setPayload(jsonObject);
		dpmJsonProduct = GsonUtil.gson.toJson(event);
	}

	@Test
	@Order(1)
	void testNewProductFromDPM() {
		try {
			testProcessSQSNewProductFromDPMHasRun = true;
			Handler handler = new Handler();
			//String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs-from-dpm.json"));
			SQSEvent event = new SQSEvent();
			event.setRecords(new ArrayList<SQSEvent.SQSMessage>());
			SQSMessage msg = new SQSMessage();
			msg.setBody(dpmJsonProduct);
			event.getRecords().add(msg);

			handler.handleRequest(event, null);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e);
		}
	}

	@Test
	@Order(2)
	void testUpdateProductFromDPM() {
		try {
			if (!testProcessSQSNewProductFromDPMHasRun)
				dpmJsonProduct = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs-from-dpm.json"));

			Handler handler = new Handler();
			//String json = 
			SQSEvent event = new SQSEvent();
			event.setRecords(new ArrayList<SQSEvent.SQSMessage>());
			SQSMessage msg = new SQSMessage();
			msg.setBody(dpmJsonProduct);
			event.getRecords().add(msg);

			handler.handleRequest(event, null);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e);
		}
	}

	@Test
	@Order(3)
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
