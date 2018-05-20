package com.ufoscout.coreutils.cache.noops;

import com.ufoscout.coreutils.cache.Cache;
import com.ufoscout.coreutils.cache.CacheManager;

/**
 * 
 * @author Francesco Cina
 *
 * 23 Sep 2011
 */
public class NoOpsCacheManager implements CacheManager {

	@Override
	public Cache getCache(String cacheName) {
		return new NoOpsCache();
	}

	@Override
	public void stopCacheManager() {
	}

}
