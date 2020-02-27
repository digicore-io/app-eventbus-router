package com.digipro.ebay.ro.ebay;

import java.util.ArrayList;

public class Item {
	private String AutoPay;
	private String BuyerProtection;
	BuyItNowPrice BuyItNowPriceObject;
	private String Country;
	private String Currency;
	private String Description;
	private String GiftIcon;
	private String HitCounter;
	private String ItemID;
	ListingDetails ListingDetailsObject;
	ListingDesigner ListingDesignerObject;
	private String ListingDuration;
	private String ListingType;
	private String Location;
	private String PaymentMethods;
	private String PayPalEmailAddress;
	PrimaryCategory PrimaryCategoryObject;
	private String PrivateListing;
	private String Quantity;
	ReservePrice ReservePriceObject;
	ReviseStatus ReviseStatusObject;
	Seller SellerObject;
	SellingStatus SellingStatusObject;
	ShippingDetails ShippingDetailsObject;
	private String ShipToLocations;
	private String Site;
	StartPrice StartPriceObject;
	private String TimeLeft;
	private String Title;
	private String HitCount;
	private String GetItFast;
	private String BuyerResponsibleForShipping;
	PictureDetails PictureDetailsObject;
	private String DispatchTimeMax;
	private String ProxyItem;
	BuyerGuaranteePrice BuyerGuaranteePriceObject;
	private String IntangibleItem;
	ReturnPolicy ReturnPolicyObject;
	ArrayList<Object> PaymentAllowedSite = new ArrayList<Object>();
	private String PostCheckoutExperienceEnabled;
	SellerProfiles SellerProfilesObject;
	ShippingPackageDetails ShippingPackageDetailsObject;
	private String RelistParentID;
	private String HideFromSearch;
	private String eBayPlus;
	private String eBayPlusEligible;
	private String IsSecureDescription;

	// Getter Methods 

	public String getAutoPay() {
		return AutoPay;
	}

	public String getBuyerProtection() {
		return BuyerProtection;
	}

	public BuyItNowPrice getBuyItNowPrice() {
		return BuyItNowPriceObject;
	}

	public String getCountry() {
		return Country;
	}

	public String getCurrency() {
		return Currency;
	}

	public String getDescription() {
		return Description;
	}

	public String getGiftIcon() {
		return GiftIcon;
	}

	public String getHitCounter() {
		return HitCounter;
	}

	public String getItemID() {
		return ItemID;
	}

	public ListingDetails getListingDetails() {
		return ListingDetailsObject;
	}

	public ListingDesigner getListingDesigner() {
		return ListingDesignerObject;
	}

	public String getListingDuration() {
		return ListingDuration;
	}

	public String getListingType() {
		return ListingType;
	}

	public String getLocation() {
		return Location;
	}

	public String getPaymentMethods() {
		return PaymentMethods;
	}

	public String getPayPalEmailAddress() {
		return PayPalEmailAddress;
	}

	public PrimaryCategory getPrimaryCategory() {
		return PrimaryCategoryObject;
	}

	public String getPrivateListing() {
		return PrivateListing;
	}

	public String getQuantity() {
		return Quantity;
	}

	public ReservePrice getReservePrice() {
		return ReservePriceObject;
	}

	public ReviseStatus getReviseStatus() {
		return ReviseStatusObject;
	}

	public Seller getSeller() {
		return SellerObject;
	}

	public SellingStatus getSellingStatus() {
		return SellingStatusObject;
	}

	public ShippingDetails getShippingDetails() {
		return ShippingDetailsObject;
	}

	public String getShipToLocations() {
		return ShipToLocations;
	}

	public String getSite() {
		return Site;
	}

	public StartPrice getStartPrice() {
		return StartPriceObject;
	}

	public String getTimeLeft() {
		return TimeLeft;
	}

	public String getTitle() {
		return Title;
	}

	public String getHitCount() {
		return HitCount;
	}

	public String getGetItFast() {
		return GetItFast;
	}

	public String getBuyerResponsibleForShipping() {
		return BuyerResponsibleForShipping;
	}

	public PictureDetails getPictureDetails() {
		return PictureDetailsObject;
	}

	public String getDispatchTimeMax() {
		return DispatchTimeMax;
	}

	public String getProxyItem() {
		return ProxyItem;
	}

	public BuyerGuaranteePrice getBuyerGuaranteePrice() {
		return BuyerGuaranteePriceObject;
	}

	public String getIntangibleItem() {
		return IntangibleItem;
	}

	public ReturnPolicy getReturnPolicy() {
		return ReturnPolicyObject;
	}

	public String getPostCheckoutExperienceEnabled() {
		return PostCheckoutExperienceEnabled;
	}

	public SellerProfiles getSellerProfiles() {
		return SellerProfilesObject;
	}

	public ShippingPackageDetails getShippingPackageDetails() {
		return ShippingPackageDetailsObject;
	}

	public String getRelistParentID() {
		return RelistParentID;
	}

	public String getHideFromSearch() {
		return HideFromSearch;
	}

	public String getEBayPlus() {
		return eBayPlus;
	}

	public String getEBayPlusEligible() {
		return eBayPlusEligible;
	}

	public String getIsSecureDescription() {
		return IsSecureDescription;
	}

	// Setter Methods 

	public void setAutoPay(String AutoPay) {
		this.AutoPay = AutoPay;
	}

	public void setBuyerProtection(String BuyerProtection) {
		this.BuyerProtection = BuyerProtection;
	}

	public void setBuyItNowPrice(BuyItNowPrice BuyItNowPriceObject) {
		this.BuyItNowPriceObject = BuyItNowPriceObject;
	}

	public void setCountry(String Country) {
		this.Country = Country;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public void setGiftIcon(String GiftIcon) {
		this.GiftIcon = GiftIcon;
	}

	public void setHitCounter(String HitCounter) {
		this.HitCounter = HitCounter;
	}

	public void setItemID(String ItemID) {
		this.ItemID = ItemID;
	}

	public void setListingDetails(ListingDetails ListingDetailsObject) {
		this.ListingDetailsObject = ListingDetailsObject;
	}

	public void setListingDesigner(ListingDesigner ListingDesignerObject) {
		this.ListingDesignerObject = ListingDesignerObject;
	}

	public void setListingDuration(String ListingDuration) {
		this.ListingDuration = ListingDuration;
	}

	public void setListingType(String ListingType) {
		this.ListingType = ListingType;
	}

	public void setLocation(String Location) {
		this.Location = Location;
	}

	public void setPaymentMethods(String PaymentMethods) {
		this.PaymentMethods = PaymentMethods;
	}

	public void setPayPalEmailAddress(String PayPalEmailAddress) {
		this.PayPalEmailAddress = PayPalEmailAddress;
	}

	public void setPrimaryCategory(PrimaryCategory PrimaryCategoryObject) {
		this.PrimaryCategoryObject = PrimaryCategoryObject;
	}

	public void setPrivateListing(String PrivateListing) {
		this.PrivateListing = PrivateListing;
	}

	public void setQuantity(String Quantity) {
		this.Quantity = Quantity;
	}

	public void setReservePrice(ReservePrice ReservePriceObject) {
		this.ReservePriceObject = ReservePriceObject;
	}

	public void setReviseStatus(ReviseStatus ReviseStatusObject) {
		this.ReviseStatusObject = ReviseStatusObject;
	}

	public void setSeller(Seller SellerObject) {
		this.SellerObject = SellerObject;
	}

	public void setSellingStatus(SellingStatus SellingStatusObject) {
		this.SellingStatusObject = SellingStatusObject;
	}

	public void setShippingDetails(ShippingDetails ShippingDetailsObject) {
		this.ShippingDetailsObject = ShippingDetailsObject;
	}

	public void setShipToLocations(String ShipToLocations) {
		this.ShipToLocations = ShipToLocations;
	}

	public void setSite(String Site) {
		this.Site = Site;
	}

	public void setStartPrice(StartPrice StartPriceObject) {
		this.StartPriceObject = StartPriceObject;
	}

	public void setTimeLeft(String TimeLeft) {
		this.TimeLeft = TimeLeft;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public void setHitCount(String HitCount) {
		this.HitCount = HitCount;
	}

	public void setGetItFast(String GetItFast) {
		this.GetItFast = GetItFast;
	}

	public void setBuyerResponsibleForShipping(String BuyerResponsibleForShipping) {
		this.BuyerResponsibleForShipping = BuyerResponsibleForShipping;
	}

	public void setPictureDetails(PictureDetails PictureDetailsObject) {
		this.PictureDetailsObject = PictureDetailsObject;
	}

	public void setDispatchTimeMax(String DispatchTimeMax) {
		this.DispatchTimeMax = DispatchTimeMax;
	}

	public void setProxyItem(String ProxyItem) {
		this.ProxyItem = ProxyItem;
	}

	public void setBuyerGuaranteePrice(BuyerGuaranteePrice BuyerGuaranteePriceObject) {
		this.BuyerGuaranteePriceObject = BuyerGuaranteePriceObject;
	}

	public void setIntangibleItem(String IntangibleItem) {
		this.IntangibleItem = IntangibleItem;
	}

	public void setReturnPolicy(ReturnPolicy ReturnPolicyObject) {
		this.ReturnPolicyObject = ReturnPolicyObject;
	}

	public void setPostCheckoutExperienceEnabled(String PostCheckoutExperienceEnabled) {
		this.PostCheckoutExperienceEnabled = PostCheckoutExperienceEnabled;
	}

	public void setSellerProfiles(SellerProfiles SellerProfilesObject) {
		this.SellerProfilesObject = SellerProfilesObject;
	}

	public void setShippingPackageDetails(ShippingPackageDetails ShippingPackageDetailsObject) {
		this.ShippingPackageDetailsObject = ShippingPackageDetailsObject;
	}

	public void setRelistParentID(String RelistParentID) {
		this.RelistParentID = RelistParentID;
	}

	public void setHideFromSearch(String HideFromSearch) {
		this.HideFromSearch = HideFromSearch;
	}

	public void setEBayPlus(String eBayPlus) {
		this.eBayPlus = eBayPlus;
	}

	public void setEBayPlusEligible(String eBayPlusEligible) {
		this.eBayPlusEligible = eBayPlusEligible;
	}

	public void setIsSecureDescription(String IsSecureDescription) {
		this.IsSecureDescription = IsSecureDescription;
	}
}
