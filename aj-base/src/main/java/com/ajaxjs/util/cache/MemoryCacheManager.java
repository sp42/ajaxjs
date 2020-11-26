package com.ajaxjs.util.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于虚拟机内存的cache管理器
 * 
 */
public class MemoryCacheManager extends ConcurrentHashMap<String, Cache<?, ?>> implements CacheManager {
	private static final long serialVersionUID = -8273827743219735439L;

	@Override
	public <K, V> Cache<K, V> getCache(String name) {
		@SuppressWarnings("unchecked")
		Cache<K, V> cache = (Cache<K, V>) get(name);

		if (cache == null) {
			cache = new MemoryCache<>();
			put(name, cache);
		}

		return cache;
	}

	@Override
	public void destroy() {
		while (!isEmpty())
			clear();
	}

	@Override
	public <V> Cache<String, V> getCache(String name, Class<V> clz) {
		@SuppressWarnings("unchecked")
		Cache<String, V> cache = (Cache<String, V>) get(name);
		if (cache == null) {
			cache = new MemoryCache<>();
			put(name, cache);
		}

		return cache;
	}
}
