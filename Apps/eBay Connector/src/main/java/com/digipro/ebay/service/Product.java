package com.digipro.ebay.service;

import java.util.Calendar;

public class Product {
	private String productId;
	private String ebayItemId;
	private String title;
	private String description;
	private String primaryImage;
	private String primaryImageAlt;
	private String price;
	private String productFamilyId;
	private Calendar created;
	private String locId;
	private String slug;
	private int quantity;
	private int status;
	private String weight;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProductFamilyId() {
		return productFamilyId;
	}

	public void setProductFamilyId(String productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getEbayItemId() {
		return ebayItemId;
	}

	public void setEbayItemId(String ebayItemId) {
		this.ebayItemId = ebayItemId;
	}

	public String getPrimaryImageAlt() {
		return primaryImageAlt;
	}

	public void setPrimaryImageAlt(String primaryImageAlt) {
		this.primaryImageAlt = primaryImageAlt;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrimaryImage() {
		return primaryImage;
	}

	public void setPrimaryImage(String primaryImage) {
		this.primaryImage = primaryImage;
	}

}
