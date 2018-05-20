package com.ufoscout.coreutils.cache.ehcache;

import com.ufoscout.coreutils.cache.AbstractCache;
import org.ehcache.Cache;
import org.ehcache.CacheManager;

/**
 * 
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public class EhCache extends AbstractCache {

	private final Cache<String, Object> ehcache;

	public EhCache(String name, CacheManager cacheManager) {
		super(name);
		ehcache = cacheManager.getCache(name, String.class, Object.class);
	}

	@Override
	public synchronized Object getValue(String key) {
		if (ehcache!=null) {
			return ehcache.get(key);
		}
        return null;
	}

	@Override
	public synchronized void put(String key, Object value) {
		if (ehcache!=null) {
			ehcache.put(key, value);
		}
	}

	@Override
	public synchronized void clear() {
		if (ehcache!=null) {
			ehcache.clear();
		}
	}
	
	@Override
	public synchronized void remove(String key) {
		if (ehcache!=null) {
			ehcache.remove(key);
		}
	}

	@Override
	public boolean contains(String key) {
		return ( (ehcache!=null) && (ehcache.get(key)!=null) );
	}

}
