package com.digipro.ebay.ro;

import com.digipro.ebay.ro.api.Data;

public class AppEntity {
	private String applicationId;
	private String companyId;
	private String digicoreEntityId;
	private String externalEntityId;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDigicoreEntityId() {
		return digicoreEntityId;
	}

	public void setDigicoreEntityId(String digicoreEntityId) {
		this.digicoreEntityId = digicoreEntityId;
	}

	public String getExternalEntityId() {
		return externalEntityId;
	}

	public void setExternalEntityId(String externalEntityId) {
		this.externalEntityId = externalEntityId;
	}

}
