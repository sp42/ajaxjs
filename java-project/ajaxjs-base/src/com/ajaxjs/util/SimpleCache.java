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

	public SimpleCache(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		this.eden = new ConcurrentHashMap<K, V>(maxCapacity);
		this.longterm = new WeakHashMap<K, V>(maxCapacity);
	}

	public V get(K k) {
		V v = this.eden.get(k);
		if (v == null) {
			lock.lock();
			try {
				v = this.longterm.get(k);
			} finally {
				lock.unlock();
			}
			if (v != null) {
				this.eden.put(k, v);
			}
		}
		return v;
	}

	public void put(K k, V v) {
		if (this.eden.size() >= maxCapacity) {
			lock.lock();
			try {
				this.longterm.putAll(this.eden);
			} finally {
				lock.unlock();
			}
			this.eden.clear();
		}
		this.eden.put(k, v);
	}
}