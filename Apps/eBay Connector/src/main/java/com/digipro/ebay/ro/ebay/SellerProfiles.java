package com.digipro.ebay.ro.ebay;

public class SellerProfiles {
 SellerShippingProfile SellerShippingProfileObject;
 SellerReturnProfile SellerReturnProfileObject;
 SellerPaymentProfile SellerPaymentProfileObject;


 // Getter Methods 

 public SellerShippingProfile getSellerShippingProfile() {
  return SellerShippingProfileObject;
 }

 public SellerReturnProfile getSellerReturnProfile() {
  return SellerReturnProfileObject;
 }

 public SellerPaymentProfile getSellerPaymentProfile() {
  return SellerPaymentProfileObject;
 }

 // Setter Methods 

 public void setSellerShippingProfile(SellerShippingProfile SellerShippingProfileObject) {
  this.SellerShippingProfileObject = SellerShippingProfileObject;
 }

 public void setSellerReturnProfile(SellerReturnProfile SellerReturnProfileObject) {
  this.SellerReturnProfileObject = SellerReturnProfileObject;
 }

 public void setSellerPaymentProfile(SellerPaymentProfile SellerPaymentProfileObject) {
  this.SellerPaymentProfileObject = SellerPaymentProfileObject;
 }
}