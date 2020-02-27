
package com.digipro.ebay.ro.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

	private String companyId;
	private String applicationId;
	private String internalEntityId;
	private String externalEntityId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getInternalEntityId() {
		return internalEntityId;
	}

	public void setInternalEntityId(String internalEntityId) {
		this.internalEntityId = internalEntityId;
	}

	public String getExternalEntityId() {
		return externalEntityId;
	}

	public void setExternalEntityId(String externalEntityId) {
		this.externalEntityId = externalEntityId;
	}

}
