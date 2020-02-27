package com.digipro.ebay.ro.ebay;

public class ShippingPackageDetails {
	private String ShippingIrregular;
	private String ShippingPackage;
	WeightMajor WeightMajorObject;
	WeightMinor WeightMinorObject;

	// Getter Methods 

	public String getShippingIrregular() {
		return ShippingIrregular;
	}

	public String getShippingPackage() {
		return ShippingPackage;
	}

	public WeightMajor getWeightMajor() {
		return WeightMajorObject;
	}

	public WeightMinor getWeightMinor() {
		return WeightMinorObject;
	}

	// Setter Methods 

	public void setShippingIrregular(String ShippingIrregular) {
		this.ShippingIrregular = ShippingIrregular;
	}

	public void setShippingPackage(String ShippingPackage) {
		this.ShippingPackage = ShippingPackage;
	}

	public void setWeightMajor(WeightMajor WeightMajorObject) {
		this.WeightMajorObject = WeightMajorObject;
	}

	public void setWeightMinor(WeightMinor WeightMinorObject) {
		this.WeightMinorObject = WeightMinorObject;
	}
}