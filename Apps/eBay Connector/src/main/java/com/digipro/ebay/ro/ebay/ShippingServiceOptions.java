package com.digipro.ebay.ro.ebay;
public class ShippingServiceOptions {
 private String ShippingService;
 ShippingServiceCost ShippingServiceCostObject;
 ShippingServiceAdditionalCost ShippingServiceAdditionalCostObject;
 private String ShippingServicePriority;
 private String ExpeditedService;
 private String ShippingTimeMin;
 private String ShippingTimeMax;
 private String FreeShipping;


 // Getter Methods 

 public String getShippingService() {
  return ShippingService;
 }

 public ShippingServiceCost getShippingServiceCost() {
  return ShippingServiceCostObject;
 }

 public ShippingServiceAdditionalCost getShippingServiceAdditionalCost() {
  return ShippingServiceAdditionalCostObject;
 }

 public String getShippingServicePriority() {
  return ShippingServicePriority;
 }

 public String getExpeditedService() {
  return ExpeditedService;
 }

 public String getShippingTimeMin() {
  return ShippingTimeMin;
 }

 public String getShippingTimeMax() {
  return ShippingTimeMax;
 }

 public String getFreeShipping() {
  return FreeShipping;
 }

 // Setter Methods 

 public void setShippingService(String ShippingService) {
  this.ShippingService = ShippingService;
 }

 public void setShippingServiceCost(ShippingServiceCost ShippingServiceCostObject) {
  this.ShippingServiceCostObject = ShippingServiceCostObject;
 }

 public void setShippingServiceAdditionalCost(ShippingServiceAdditionalCost ShippingServiceAdditionalCostObject) {
  this.ShippingServiceAdditionalCostObject = ShippingServiceAdditionalCostObject;
 }

 public void setShippingServicePriority(String ShippingServicePriority) {
  this.ShippingServicePriority = ShippingServicePriority;
 }

 public void setExpeditedService(String ExpeditedService) {
  this.ExpeditedService = ExpeditedService;
 }

 public void setShippingTimeMin(String ShippingTimeMin) {
  this.ShippingTimeMin = ShippingTimeMin;
 }

 public void setShippingTimeMax(String ShippingTimeMax) {
  this.ShippingTimeMax = ShippingTimeMax;
 }

 public void setFreeShipping(String FreeShipping) {
  this.FreeShipping = FreeShipping;
 }
}