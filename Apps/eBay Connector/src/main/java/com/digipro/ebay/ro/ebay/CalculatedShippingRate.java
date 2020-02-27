package com.digipro.ebay.ro.ebay;
public class CalculatedShippingRate {
 WeightMajor WeightMajorObject;
 WeightMinor WeightMinorObject;


 // Getter Methods 

 public WeightMajor getWeightMajor() {
  return WeightMajorObject;
 }

 public WeightMinor getWeightMinor() {
  return WeightMinorObject;
 }

 // Setter Methods 

 public void setWeightMajor(WeightMajor WeightMajorObject) {
  this.WeightMajorObject = WeightMajorObject;
 }

 public void setWeightMinor(WeightMinor WeightMinorObject) {
  this.WeightMinorObject = WeightMinorObject;
 }
}