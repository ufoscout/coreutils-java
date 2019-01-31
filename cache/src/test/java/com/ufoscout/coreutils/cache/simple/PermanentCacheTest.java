package com.ufoscout.coreutils.cache.simple;

import com.ufoscout.coreutils.cache.BaseTest;
import com.ufoscout.coreutils.cache.Cache;
import com.ufoscout.coreutils.cache.CacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * @author Francesco Cina
 *
 * 23 Sep 2011
 */
public class PermanentCacheTest extends BaseTest {

	private static String CACHE_NAME = "query.ObjectShortTermCache";
	private CacheManager cacheService;
	private int cacheSize = 10;

	@BeforeEach
	public void setUp() throws Exception {
		Cache cache = new SimpleCache(CACHE_NAME, cacheSize);
		List<Cache> caches = new ArrayList<Cache>();
		caches.add(cache);
		cacheService = new SimpleCacheManager(caches);
		assertNotNull(cacheService);
	}

	@Test
	public void testCache1() throws Exception {
		Cache emptyStringCache = cacheService.getCache("");
		assertNotNull( emptyStringCache );
		assertNull( emptyStringCache.get("hello") );
		assertNull( emptyStringCache.get(null) );
		emptyStringCache.put("key", "value");
		emptyStringCache.put("key", null);
		emptyStringCache.put(null, "value");
		emptyStringCache.put(null, null);
	}

	@Test
	public void testCache2() throws Exception {
		Cache nullStringCache = cacheService.getCache("");
		assertNotNull( nullStringCache );
		assertNull( nullStringCache.get("hello") );
		assertNull( nullStringCache.get(null) );
		nullStringCache.put("key", "value");
		nullStringCache.put("key", null);
		nullStringCache.put(null, "value");
		nullStringCache.put(null, null);
	}

	@Test
	public void testCache3() throws Exception {
		Cache cache = cacheService.getCache(CACHE_NAME);
		assertNotNull( cache );
		String key = "test-key-" + new Date().getTime();
		assertNull( cache.get(key) );
		assertNull( cache.get(null) );
		
		cache.put(key, "value");
		assertNotNull(cache.get(key));
		assertEquals( "value" , (String) cache.get(key) );
		
		cache.clear();
		assertNull(cache.get(key));
		
		cache.put("key", null);
		cache.put(null, "value");
		cache.put(null, null);
		
		cache.clear();
	}

	@Test
	public void testCache4() throws Exception {
		Cache cache = cacheService.getCache(CACHE_NAME);
		assertNotNull( cache );
		String key1 = "test-key1-" + new Date().getTime();
		String key2 = "test-key2-" + new Date().getTime();
		String key3 = "test-key3-" + new Date().getTime();
		assertNull( cache.get(key1) );
		assertNull( cache.get(key2) );
		assertNull( cache.get(key3) );
		
		cache.put(key1, "value1");
		assertNotNull(cache.get(key1));
		assertEquals( "value1" , (String) cache.get(key1) );
		
		cache.put(key2, "value2");
		assertNotNull(cache.get(key2));
		assertEquals( "value2" , (String) cache.get(key2) );
		
		cache.put(key3, "value3");
		assertNotNull(cache.get(key3));
		assertEquals( "value3" , (String) cache.get(key3) );
		
		cache.remove(key2);
		assertNotNull(cache.get(key1));
		assertNull(cache.get(key2));
		assertNotNull(cache.get(key3));
		
		cache.clear();
		assertNull(cache.get(key1));
		assertNull(cache.get(key2));
		assertNull(cache.get(key3));
		cache.clear();
	}

	@Test
	public void testCacheSize() {
		Cache cache = cacheService.getCache(CACHE_NAME);
		cache.clear();
		
		String first = "first";
		String second = "second";
		
		cache.put(first, "");
		cache.put(second, "");
		assertNotNull(cache.get(first));
		assertNotNull(cache.get(second));
		
		for (int i=0; i<cacheSize-2; i++) {
			cache.put("key" + i, "");
		}
		
		assertNotNull(cache.get(first));
		assertNotNull(cache.get(second));
		
		cache.put("new-key-1", "");
		assertNull(cache.get(first));
		assertNotNull(cache.get(second));
		
		cache.put("new-key-2", "");
		assertNull(cache.get(first));
		assertNull(cache.get(second));
		
	}
}
