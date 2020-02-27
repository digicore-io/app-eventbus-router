package com.digipro.ebay.ro.ebay;
public class ShippingDetails {
 private String ApplyShippingDiscount;
 CalculatedShippingRate CalculatedShippingRateObject;
 SalesTax SalesTaxObject;
 ShippingServiceOptions ShippingServiceOptionsObject;
 private String ShippingType;
 private String ThirdPartyCheckout;
 private String ShippingDiscountProfileID;
 private String InternationalShippingDiscountProfileID;
 private String SellerExcludeShipToLocationsPreference;


 // Getter Methods 

 public String getApplyShippingDiscount() {
  return ApplyShippingDiscount;
 }

 public CalculatedShippingRate getCalculatedShippingRate() {
  return CalculatedShippingRateObject;
 }

 public SalesTax getSalesTax() {
  return SalesTaxObject;
 }

 public ShippingServiceOptions getShippingServiceOptions() {
  return ShippingServiceOptionsObject;
 }

 public String getShippingType() {
  return ShippingType;
 }

 public String getThirdPartyCheckout() {
  return ThirdPartyCheckout;
 }

 public String getShippingDiscountProfileID() {
  return ShippingDiscountProfileID;
 }

 public String getInternationalShippingDiscountProfileID() {
  return InternationalShippingDiscountProfileID;
 }

 public String getSellerExcludeShipToLocationsPreference() {
  return SellerExcludeShipToLocationsPreference;
 }

 // Setter Methods 

 public void setApplyShippingDiscount(String ApplyShippingDiscount) {
  this.ApplyShippingDiscount = ApplyShippingDiscount;
 }

 public void setCalculatedShippingRate(CalculatedShippingRate CalculatedShippingRateObject) {
  this.CalculatedShippingRateObject = CalculatedShippingRateObject;
 }

 public void setSalesTax(SalesTax SalesTaxObject) {
  this.SalesTaxObject = SalesTaxObject;
 }

 public void setShippingServiceOptions(ShippingServiceOptions ShippingServiceOptionsObject) {
  this.ShippingServiceOptionsObject = ShippingServiceOptionsObject;
 }

 public void setShippingType(String ShippingType) {
  this.ShippingType = ShippingType;
 }

 public void setThirdPartyCheckout(String ThirdPartyCheckout) {
  this.ThirdPartyCheckout = ThirdPartyCheckout;
 }

 public void setShippingDiscountProfileID(String ShippingDiscountProfileID) {
  this.ShippingDiscountProfileID = ShippingDiscountProfileID;
 }

 public void setInternationalShippingDiscountProfileID(String InternationalShippingDiscountProfileID) {
  this.InternationalShippingDiscountProfileID = InternationalShippingDiscountProfileID;
 }

 public void setSellerExcludeShipToLocationsPreference(String SellerExcludeShipToLocationsPreference) {
  this.SellerExcludeShipToLocationsPreference = SellerExcludeShipToLocationsPreference;
 }
}