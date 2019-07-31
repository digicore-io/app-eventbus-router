package com.digipro.ebay.model;

import java.util.Calendar;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.digipro.ebay.dao.EventLogDao;

@DynamoDBTable(tableName = EventLogDao.TABLE)
public class EventLog extends BaseEntity implements EntityIdI {

	private String id;
	private String applicationId;
	private String eventRequest;
	private String status;
	private String info;
	private Calendar created;

	public EventLog() {
	}

	public EventLog(String id) {
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

	@DynamoDBAttribute(attributeName = "eventRequest")
	public String getEventRequest() {
		return eventRequest;
	}

	public void setEventRequest(String eventRequest) {
		this.eventRequest = eventRequest;
	}

	@DynamoDBAttribute(attributeName = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@DynamoDBAttribute(attributeName = "info")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@DynamoDBAttribute(attributeName = "created")
	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

}
