package com.ufoscout.coreutils.cache.noops;

import com.ufoscout.coreutils.cache.Cache;

import java.io.Serializable;

/**
 * 
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public class NoOpsCache implements Cache {

	@Override
	public Serializable get(String key) {
		return null;
	}

	@Override
	public void put(String key, Object value) {
	}

	@Override
	public void clear() {
	}

	@Override
	public void remove(String key) {
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return null;
	}

	@Override
	public boolean contains(String key) {
		return false;
	}

}
