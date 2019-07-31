
package com.digipro.ebay.ro;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("familyId")
	@Expose
	private String familyId;
	@SerializedName("sku")
	@Expose
	private String sku;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("shortDescription")
	@Expose
	private String shortDescription;
	@SerializedName("slug")
	@Expose
	private String slug;
	@SerializedName("cost")
	@Expose
	private String cost;
	@SerializedName("price")
	@Expose
	private String price;
	@SerializedName("purchasePrice")
	@Expose
	private String purchasePrice;
	@SerializedName("taxRateId")
	@Expose
	private String taxRateId;
	@SerializedName("locID")
	@Expose
	private String locID;
	@SerializedName("metaTitle")
	@Expose
	private String metaTitle;
	@SerializedName("metaDescription")
	@Expose
	private String metaDescription;
	@SerializedName("metaKeyword")
	@Expose
	private String metaKeyword;
	@SerializedName("tags")
	@Expose
	private String tags;
	@SerializedName("attachType")
	@Expose
	private String attachType;
	@SerializedName("attachment")
	@Expose
	private String attachment;
	@SerializedName("createdAt")
	@Expose
	private String createdAt;
	@SerializedName("albumId")
	@Expose
	private String albumId;
	@SerializedName("sendEventTicket")
	@Expose
	private String sendEventTicket;
	@SerializedName("startDate")
	@Expose
	private String startDate;
	@SerializedName("startTime")
	@Expose
	private String startTime;
	@SerializedName("endDate")
	@Expose
	private String endDate;
	@SerializedName("endTime")
	@Expose
	private String endTime;
	@SerializedName("timeZone")
	@Expose
	private String timeZone;
	@SerializedName("postEventId")
	@Expose
	private String postEventId;
	@SerializedName("showSharing")
	@Expose
	private String showSharing;
	@SerializedName("qty")
	@Expose
	private int qty;
	@SerializedName("minPurchaseQty")
	@Expose
	private int minPurchaseQty;
	@SerializedName("maxPurchaseQty")
	@Expose
	private int maxPurchaseQty;
	@SerializedName("cartBtnLabel")
	@Expose
	private String cartBtnLabel;
	@SerializedName("featured")
	@Expose
	private int featured;
	@SerializedName("recurring")
	@Expose
	private String recurring;
	@SerializedName("recurringFrequency")
	@Expose
	private String recurringFrequency;
	@SerializedName("recurringStartDay")
	@Expose
	private String recurringStartDay;

	@SerializedName("status")
	@Expose
	private int status;
	@SerializedName("enableFreeShipping")
	@Expose
	private int enableFreeShipping;
	@SerializedName("visibleWeb")
	@Expose
	private int visibleWeb;
	@SerializedName("visibleVirtual")
	@Expose
	private int visibleVirtual;
	@SerializedName("images")
	@Expose
	private List<String> images = null;
	@SerializedName("ebayPaymentPolicyId")
	@Expose
	private String ebayPaymentPolicyId;
	@SerializedName("ebayFulfillmentPolicyId")
	@Expose
	private String ebayFulfillmentPolicyId;
	@SerializedName("ebayReturnPolicyId")
	@Expose
	private String ebayReturnPolicyId;
	@SerializedName("ebayCategoryId")
	@Expose
	private String ebayCategoryId;
	private boolean ebayPublish;
	private int ebayConditionId;

	public boolean isEbayPublish() {
		return ebayPublish;
	}

	public void setEbayPublish(boolean ebayPublish) {
		this.ebayPublish = ebayPublish;
	}

	public int getFeatured() {
		return featured;
	}

	public void setFeatured(int featured) {
		this.featured = featured;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getEbayConditionId() {
		return ebayConditionId;
	}

	public void setEbayConditionId(int ebayConditionId) {
		this.ebayConditionId = ebayConditionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getTaxRateId() {
		return taxRateId;
	}

	public void setTaxRateId(String taxRateId) {
		this.taxRateId = taxRateId;
	}

	public String getLocID() {
		return locID;
	}

	public void setLocID(String locID) {
		this.locID = locID;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getMetaKeyword() {
		return metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getSendEventTicket() {
		return sendEventTicket;
	}

	public void setSendEventTicket(String sendEventTicket) {
		this.sendEventTicket = sendEventTicket;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPostEventId() {
		return postEventId;
	}

	public void setPostEventId(String postEventId) {
		this.postEventId = postEventId;
	}

	public String getShowSharing() {
		return showSharing;
	}

	public void setShowSharing(String showSharing) {
		this.showSharing = showSharing;
	}

	public String getCartBtnLabel() {
		return cartBtnLabel;
	}

	public void setCartBtnLabel(String cartBtnLabel) {
		this.cartBtnLabel = cartBtnLabel;
	}

	public String getRecurring() {
		return recurring;
	}

	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}

	public String getRecurringFrequency() {
		return recurringFrequency;
	}

	public void setRecurringFrequency(String recurringFrequency) {
		this.recurringFrequency = recurringFrequency;
	}

	public String getRecurringStartDay() {
		return recurringStartDay;
	}

	public void setRecurringStartDay(String recurringStartDay) {
		this.recurringStartDay = recurringStartDay;
	}

	public int getEnableFreeShipping() {
		return enableFreeShipping;
	}

	public void setEnableFreeShipping(int enableFreeShipping) {
		this.enableFreeShipping = enableFreeShipping;
	}

	public int getVisibleWeb() {
		return visibleWeb;
	}

	public void setVisibleWeb(int visibleWeb) {
		this.visibleWeb = visibleWeb;
	}

	public int getVisibleVirtual() {
		return visibleVirtual;
	}

	public void setVisibleVirtual(int visibleVirtual) {
		this.visibleVirtual = visibleVirtual;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getMinPurchaseQty() {
		return minPurchaseQty;
	}

	public void setMinPurchaseQty(int minPurchaseQty) {
		this.minPurchaseQty = minPurchaseQty;
	}

	public int getMaxPurchaseQty() {
		return maxPurchaseQty;
	}

	public void setMaxPurchaseQty(int maxPurchaseQty) {
		this.maxPurchaseQty = maxPurchaseQty;
	}

	public String getEbayPaymentPolicyId() {
		return ebayPaymentPolicyId;
	}

	public void setEbayPaymentPolicyId(String ebayPaymentPolicyId) {
		this.ebayPaymentPolicyId = ebayPaymentPolicyId;
	}

	public String getEbayFulfillmentPolicyId() {
		return ebayFulfillmentPolicyId;
	}

	public void setEbayFulfillmentPolicyId(String ebayFulfillmentPolicyId) {
		this.ebayFulfillmentPolicyId = ebayFulfillmentPolicyId;
	}

	public String getEbayReturnPolicyId() {
		return ebayReturnPolicyId;
	}

	public void setEbayReturnPolicyId(String ebayReturnPolicyId) {
		this.ebayReturnPolicyId = ebayReturnPolicyId;
	}

	public String getEbayCategoryId() {
		return ebayCategoryId;
	}

	public void setEbayCategoryId(String ebayCategoryId) {
		this.ebayCategoryId = ebayCategoryId;
	}

}
