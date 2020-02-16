package com.serverless;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.util.IOUtils;
import com.digipro.ebay.ro.DpmPayload;
import com.digipro.ebay.service.Product;

import io.digicore.lambda.GsonUtil;
import io.digicore.lambda.ro.CompanyEventRo;

public class TestSQS {

	private static final String DEV_QUEUE = "https://sqs.us-east-1.amazonaws.com/997357843271/EventBus-Router-Queue";
	private static final String PROD_QUEUE = "https://sqs.us-east-1.amazonaws.com/912918454053/EventBus-RoutingQueue";

	@Test
	public void sendSqs() {
		try {
			String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("event-sqs-from-dpm.json"));
			CompanyEventRo event = GsonUtil.gson.fromJson(json, CompanyEventRo.class);
			event.getPayload().getAsJsonObject().remove("qty");
			event.getPayload().getAsJsonObject().addProperty("qty", (new Random()).nextInt(50) + 1);

			//Product product = GsonUtil.gson.fromJson(event.getp, Product.class);
			//product.setQuantity((new Random()).nextInt(50) + 1);
			System.err.println("Quantity " + event.getPayload().getAsJsonObject().get("qty").getAsString());
			json = GsonUtil.gson.toJson(event);
			SQSMessage msg = new SQSMessage();
			msg.setBody(json);
			System.err.println(json);
			AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("AKIA5JDROD4S366RQX23", "aUzmfgK4ynN0WPsy0y/K+Dh/YQqw6NB/w01JhrB0")))
					.withRegion(Regions.US_EAST_1).build();

			SendMessageResult result = sqs.sendMessage(DEV_QUEUE, json);
			System.err.println(result.getMessageId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
