package com.digipro.ebay.service;

public class ProductFamily {
	private int familyId;
	private String category = "store";
	private String familyName;
	private String familySlug;
	private int orgId;
	private String pageTitle;

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilySlug() {
		return familySlug;
	}

	public void setFamilySlug(String familySlug) {
		this.familySlug = familySlug;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

}
