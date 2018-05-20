package com.ufoscout.coreutils.cache;

/**
 * 
 * @author Francesco Cina'
 *
 * 24/set/2011
 */
public abstract class AbstractCache implements Cache {

	private final String name;

	public AbstractCache(String name) {
		this.name = name;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final synchronized <T> T get(String key, Class<T> clazz) {
		return (T) get(key);
	}
	
	@Override
	public final synchronized Object get(String key) {
		return getValue(key);
	}
	
	protected abstract Object getValue(String key);

	@Override
	public String getName() {
		return name;
	}

}
