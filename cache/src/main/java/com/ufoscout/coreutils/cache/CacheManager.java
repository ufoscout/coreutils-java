package com.ufoscout.coreutils.cache;

/**
 * 
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public interface CacheManager {

	Cache getCache(String cacheName);

	void stopCacheManager();
	
}
