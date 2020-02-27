package com.digipro.ebay.ro.ebay;
public class SalesTax {
 private String SalesTaxPercent;
 private String ShippingIncludedInTax;


 // Getter Methods 

 public String getSalesTaxPercent() {
  return SalesTaxPercent;
 }

 public String getShippingIncludedInTax() {
  return ShippingIncludedInTax;
 }

 // Setter Methods 

 public void setSalesTaxPercent(String SalesTaxPercent) {
  this.SalesTaxPercent = SalesTaxPercent;
 }

 public void setShippingIncludedInTax(String ShippingIncludedInTax) {
  this.ShippingIncludedInTax = ShippingIncludedInTax;
 }
}