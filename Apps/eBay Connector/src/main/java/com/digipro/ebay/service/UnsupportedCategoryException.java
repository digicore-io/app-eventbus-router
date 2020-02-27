package com.digipro.ebay.service;

public class UnsupportedCategoryException extends Exception {

	public UnsupportedCategoryException() {
	}

	public UnsupportedCategoryException(String message) {
		super(message);
	}

	public UnsupportedCategoryException(Throwable cause) {
		super(cause);
	}

	public UnsupportedCategoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedCategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
