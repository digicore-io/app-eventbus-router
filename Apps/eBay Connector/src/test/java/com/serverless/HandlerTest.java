package com.serverless;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.util.IOUtils;
import com.digipro.ebay.service.GsonUtil;
import com.google.gson.JsonObject;

/**
 * TODO: Ensure we have 100% test coverage and cleanup 
 * i.e. remove listing from ebay so it can be re added
 */
class HandlerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testProcessSQSNewProduct() {
		try {
			Handler handler = new Handler();
			String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs.json"));
			JsonObject object = GsonUtil.gson.fromJson(json, JsonObject.class);
			//			System.err.println(object.get("Records"));
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("Records", object.get("Records").getAsJsonArray().toString());
			handler.handleRequest(input, null);

		} catch (Exception e) {
			fail(e);
		}
	}

}
