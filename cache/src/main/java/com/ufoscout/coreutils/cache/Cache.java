package com.ufoscout.coreutils.cache;

/**
 * 
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public interface Cache {
	
	<T> T get(String key, Class<T> clazz);
	
	Object get(String key);
	
	void put(String key, Object value);
	
	void remove(String key);
	
	void clear();

	boolean contains(String key);
	
	String getName();
	
}
