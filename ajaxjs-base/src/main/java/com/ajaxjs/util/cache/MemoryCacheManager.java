package com.ajaxjs.util.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于虚拟机内存的cache管理器
 * 
 */
@SuppressWarnings("rawtypes")
public class MemoryCacheManager extends ConcurrentHashMap<String, Cache> implements CacheManager {
	private static final long serialVersionUID = -8273827743219735439L;

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Cache<K, V> getCache(String name) {
		Cache<K, V> cache = get(name);

		if (cache == null) {
			cache = new MemoryCache<K, V>();
			put(name, cache);
		}

		return cache;
	}

	@Override
	public void destroy() {
		while (!isEmpty())
			clear();
	}
}
