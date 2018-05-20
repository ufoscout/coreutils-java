package com.ufoscout.coreutils.cache.ehcache;

import com.ufoscout.coreutils.cache.Cache;
import org.ehcache.CacheManager;


/**
 * 
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public class EhCacheManager implements com.ufoscout.coreutils.cache.CacheManager {

	private final CacheManager cacheManager;

	public EhCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@Override
	public Cache getCache(String cacheName) {
		return new EhCache(cacheName, cacheManager);
	}

	@Override
	public void stopCacheManager() {
		cacheManager.close();
	}

}
