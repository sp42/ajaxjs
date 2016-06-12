package com.ajaxjs.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class SimpleCache<K, V> {
	private final Lock lock = new ReentrantLock();
	private final int maxCapacity;
	private final Map<K, V> eden;
	private final Map<K, V> longterm;

	/**
	 * 
	 * @param maxCapacity 加载因子，加载因子是一个比例，当HashMap的数据大小>=容量*加载因子时，HashMap会将容量扩容
	 */
	public SimpleCache(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		eden = new ConcurrentHashMap<>(maxCapacity);
		longterm = new WeakHashMap<>(maxCapacity);
	}

	/**
	 * 获取缓存中数据
	 * @param k 键
	 * @return 值
	 */
	public V get(K k) {
		V v = eden.get(k);
		if (v == null) {
			lock.lock();
			try {
				v = longterm.get(k);
			} finally {
				lock.unlock();
			}
			if (v != null) {
				eden.put(k, v);
			}
		}
		return v;
	}

	/**
	 * 保存缓存数据
	 * @param k 键
	 * @param v 值
	 */
	public void put(K k, V v) {
		if (eden.size() >= maxCapacity) {
			lock.lock();
			try {
				longterm.putAll(eden);
			} finally {
				lock.unlock();
			}
			eden.clear();
		}
		eden.put(k, v);
	}
}