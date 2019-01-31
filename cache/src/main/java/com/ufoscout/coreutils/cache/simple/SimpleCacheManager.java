package com.ufoscout.coreutils.cache.simple;

import com.ufoscout.coreutils.cache.Cache;
import com.ufoscout.coreutils.cache.noops.NoOpsCache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Francesco Cina'
 *
 * 24/set/2011
 */
public class SimpleCacheManager implements com.ufoscout.coreutils.cache.CacheManager, Serializable {

	private static final long serialVersionUID = 1L;
	private final Map<String, Cache> cachesMap = new HashMap<String, Cache>();

	public SimpleCacheManager(List<Cache> caches) {
		for (Cache cache : caches) {
			cachesMap.put(cache.getName(), cache);
		}
	}

	@Override
	public Cache getCache(String cacheName) {
		if (cachesMap.containsKey(cacheName)) {
			return cachesMap.get(cacheName);
		}
		return new NoOpsCache();
	}

	@Override
	public void stopCacheManager() {
		cachesMap.clear();		
	}

}
