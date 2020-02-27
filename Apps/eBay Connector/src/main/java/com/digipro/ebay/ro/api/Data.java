
package com.digipro.ebay.ro.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

	@SerializedName("itemId")
	@Expose
	private String itemId;

	private String productId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

}
