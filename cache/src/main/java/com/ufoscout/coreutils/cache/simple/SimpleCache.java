package com.ufoscout.coreutils.cache.simple;

import com.ufoscout.coreutils.cache.AbstractCache;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * A Cache that holds objects for a fixed maximum time
 * 
 * @author Francesco Cina
 *
 * 26/lug/2011
 */
public class SimpleCache extends AbstractCache {

	private final Map<String, ICacheElement> map;
	private final long timeToLiveMilliSeconds;
	public final int maxSize;

	public SimpleCache(String name, int maxSize) {
		this(name, maxSize, Integer.MAX_VALUE);
	}

	public SimpleCache(String name, int maxSize, int timeToLiveSeconds) {
		super(name);
		this.maxSize = maxSize;
		this.timeToLiveMilliSeconds = timeToLiveSeconds*1000l;
		
		map = new LinkedHashMap<String, ICacheElement>() {
			private static final long serialVersionUID = 1L;
			
            @Override
			protected boolean removeEldestEntry(Map.Entry<String, ICacheElement> oldest) {
                return size() > maxSize;
            }
			
		};
	}
	
	@Override
	public synchronized Object getValue(String key) {
		ICacheElement cacheElement = map.get(key);
		if ( cacheElement!=null ) {
			if ( cacheElement.getTimestamp() < new Date().getTime()) {
				remove(key);
				return null;
			}
			return cacheElement.getValue();
		}
		return null;
	}

	@Override
	public synchronized void put(String key, Object value) {
		map.put(key, new CacheElement(value, new Date().getTime() + timeToLiveMilliSeconds));
	}

	@Override
	public synchronized void remove(String key) {
		map.remove(key);
	}

	@Override
	public synchronized void clear() {
		map.clear();
	}

	@Override
	public boolean contains(String key) {
		return map.containsKey(key);
	}
	
	public int getSize() {
		return map.size();
	}
	
}
