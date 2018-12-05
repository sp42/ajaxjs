package com.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * http://www.importnew.com/16264.html 10行Java代码实现最近被使用（LRU）缓存
 * 
 * @author Frank Cheung
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -7603398077756864791L;

	private int cacheSize;

	public LRUCache(int cacheSize) {
		super(16, 0.75f, true);
		this.cacheSize = cacheSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() >= cacheSize;
	}
}