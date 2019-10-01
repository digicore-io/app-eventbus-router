package com.digipro.ebay.model;

import java.util.Calendar;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.digipro.ebay.dao.CompanyAppEventDao;
import com.digipro.ebay.dao.EventLogDao;

@DynamoDBTable(tableName = CompanyAppEventDao.TABLE)
public class CompanyAppEvent extends BaseEntity implements EntityIdI {

	private String id;
	private String applicationId;
	private String companyId;
	private String event;
	private String authToken;
	private String config;

	public CompanyAppEvent() {
	}

	public CompanyAppEvent(String id) {
		this.id = id;
	}

	@Override
	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "applicationId")
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@DynamoDBAttribute(attributeName = "companyId")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@DynamoDBAttribute(attributeName = "event")
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@DynamoDBAttribute(attributeName = "authToken")
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@DynamoDBAttribute(attributeName = "config")
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

}
