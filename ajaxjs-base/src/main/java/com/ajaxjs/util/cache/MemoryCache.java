package com.ajaxjs.util.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存管理cache
 * 
 */
public class MemoryCache<K, V> extends ConcurrentHashMap<K, V> implements Cache<K, V>  {
	private static final long serialVersionUID = -2438531796836393205L;
}
