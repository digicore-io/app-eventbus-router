package com.digipro.ebay.ro.ebay;

public class SellingStatus {
	private String BidCount;
	BidIncrement BidIncrementObject;
	ConvertedCurrentPrice ConvertedCurrentPriceObject;
	CurrentPrice CurrentPriceObject;
	private String LeadCount;
	MinimumToBid MinimumToBidObject;
	private String QuantitySold;
	private String ReserveMet;
	private String SecondChanceEligible;
	private String ListingStatus;
	private String QuantitySoldByPickupInStore;

	// Getter Methods 

	public String getBidCount() {
		return BidCount;
	}

	public BidIncrement getBidIncrement() {
		return BidIncrementObject;
	}

	public ConvertedCurrentPrice getConvertedCurrentPrice() {
		return ConvertedCurrentPriceObject;
	}

	public CurrentPrice getCurrentPrice() {
		return CurrentPriceObject;
	}

	public String getLeadCount() {
		return LeadCount;
	}

	public MinimumToBid getMinimumToBid() {
		return MinimumToBidObject;
	}

	public String getQuantitySold() {
		return QuantitySold;
	}

	public String getReserveMet() {
		return ReserveMet;
	}

	public String getSecondChanceEligible() {
		return SecondChanceEligible;
	}

	public String getListingStatus() {
		return ListingStatus;
	}

	public String getQuantitySoldByPickupInStore() {
		return QuantitySoldByPickupInStore;
	}

	// Setter Methods 

	public void setBidCount(String BidCount) {
		this.BidCount = BidCount;
	}

	public void setBidIncrement(BidIncrement BidIncrementObject) {
		this.BidIncrementObject = BidIncrementObject;
	}

	public void setConvertedCurrentPrice(ConvertedCurrentPrice ConvertedCurrentPriceObject) {
		this.ConvertedCurrentPriceObject = ConvertedCurrentPriceObject;
	}

	public void setCurrentPrice(CurrentPrice CurrentPriceObject) {
		this.CurrentPriceObject = CurrentPriceObject;
	}

	public void setLeadCount(String LeadCount) {
		this.LeadCount = LeadCount;
	}

	public void setMinimumToBid(MinimumToBid MinimumToBidObject) {
		this.MinimumToBidObject = MinimumToBidObject;
	}

	public void setQuantitySold(String QuantitySold) {
		this.QuantitySold = QuantitySold;
	}

	public void setReserveMet(String ReserveMet) {
		this.ReserveMet = ReserveMet;
	}

	public void setSecondChanceEligible(String SecondChanceEligible) {
		this.SecondChanceEligible = SecondChanceEligible;
	}

	public void setListingStatus(String ListingStatus) {
		this.ListingStatus = ListingStatus;
	}

	public void setQuantitySoldByPickupInStore(String QuantitySoldByPickupInStore) {
		this.QuantitySoldByPickupInStore = QuantitySoldByPickupInStore;
	}
}
