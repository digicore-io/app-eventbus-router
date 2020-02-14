package com.digipro.ebay.service;

import java.util.HashSet;
import java.util.Set;

public class CategoryConfig {

	private Set<String> categoryFilter = new HashSet<String>();
	private int categoryLevel = 1;

	public Set<String> getCategoryFilter() {
		return categoryFilter;
	}

	public void setCategoryFilter(Set<String> categoryFilter) {
		this.categoryFilter = categoryFilter;
	}

	public int getCategoryLevel() {
		return categoryLevel;
	}

	public void setCategoryLevel(int categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

}
