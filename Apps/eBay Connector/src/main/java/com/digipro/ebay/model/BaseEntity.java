package com.digipro.ebay.model;

import com.google.gson.annotations.Expose;

public class BaseEntity {

	@Expose(serialize = false, deserialize = false)
	protected transient String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
