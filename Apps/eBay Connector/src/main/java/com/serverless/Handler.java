package com.serverless;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.digipro.ebay.ro.Record;
import com.digipro.ebay.service.CoreService;
import com.digipro.ebay.service.GsonUtil;
import com.google.gson.reflect.TypeToken;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		if (input.containsKey("Records")) { //SQS

			List<Record> records = GsonUtil.gson.fromJson((String) input.get("Records"), new TypeToken<List<Record>>() {
			}.getType());

			CoreService service = new CoreService();
			for (Record record : records)
				service.processMessage(record);
		}

		return null;
	}
}
