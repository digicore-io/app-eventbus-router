
package com.serverless;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

	@SerializedName("companyId")
	@Expose
	private String companyId;
	@SerializedName("event")
	@Expose
	private String event;
	@SerializedName("applicationId")
	@Expose
	private String applicationId;
	@SerializedName("payload")
	@Expose
	private String payload;
	@SerializedName("companyAppEventId")
	@Expose
	private String companyAppEventId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getCompanyAppEventId() {
		return companyAppEventId;
	}

	public void setCompanyAppEventId(String companyAppEventId) {
		this.companyAppEventId = companyAppEventId;
	}

}
