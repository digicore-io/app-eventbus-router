package com.digipro.ebay.dao;

public interface IDaoBase<T> {
	public T save(T entity);

	public T delete(T entity);

	public T load(String hashkey);

	public T load(String hashkey, String rangekey);
}
