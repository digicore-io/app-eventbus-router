package com.digipro.ebay.service;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;

public class ParamUtils {
	static AWSSimpleSystemsManagement client;

	public static String getParameter(String name) {
		try {
			if (client == null)
				client = AWSSimpleSystemsManagementClientBuilder.defaultClient();

			GetParameterRequest pr = new GetParameterRequest();
			pr.withName(name).setWithDecryption(true);
			GetParameterResult result = client.getParameter(pr);
			return result.getParameter().getValue();
		} catch (ParameterNotFoundException e) {
			throw new RuntimeException("Couldn't get the " + name + " parameter from SSM");
		}
	}
}
