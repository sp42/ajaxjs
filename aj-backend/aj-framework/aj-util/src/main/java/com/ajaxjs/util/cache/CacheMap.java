package com.ajaxjs.util.cache;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: SpringTest
 * @description: 基于redis 懒删除思想设计带超时清除功能的map
 * @author: hu.chen
 * @createDate: 2021年05月28日 10:39
 **/
public class CacheMap<K, V> implements Map<K, V>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9133802975194449784L;

	/**
	 * 存储具体的key-value
	 */
	private volatile ConcurrentHashMap<K, V> storageMap;

	/**
	 * 记录各个key的超时时间
	 */
	private volatile ConcurrentHashMap<K, Long> timeMap;

	/**
	 * 整个map集合所有的key的过期时间：-1表示不超时,单位毫秒
	 */
	private long GLOBAL_TIMEOUT = -1;

	/**
	 * 初始化map
	 */
	public CacheMap() {
		this(16, -1);
	}

	/**
	 * 创建Map集合并指定整个集合的初始大小
	 *
	 * @param initSize :初始大小
	 */
	public CacheMap(int initSize) {
		this(initSize, -1);
	}

	/**
	 * 创建Map集合并指定整个集合的初始大小和整个集合的超时时间，单位秒
	 *
	 * @param initSize
	 */
	public CacheMap(int initSize, long timeOut) {
		storageMap = new ConcurrentHashMap<>(initSize);
		timeMap = new ConcurrentHashMap<>(initSize);

		if (timeOut != -1)
			this.GLOBAL_TIMEOUT = timeOut * 1000;
	}

	/**
	 * 获取
	 *
	 * @param key
	 * @return
	 */
	@Override
	public V get(Object key) {
		// 如果当前的key已经过期则进行删除
		@SuppressWarnings("unchecked")
		boolean b = delPastKey((K) key);
		if (b)
			return null;

		return storageMap.get(key);
	}

	/**
	 * 设置，并指定超时时间，单位秒
	 *
	 * @param key
	 * @param value
	 * @param timeOut
	 * @return
	 */
	public V put(K key, V value, long timeOut) {
		timeMap.put(key, System.currentTimeMillis() + timeOut * 1000);
		return storageMap.put(key, value);
	}

	/**
	 * 设置，
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public V put(K key, V value) {
		if (GLOBAL_TIMEOUT != -1)
			timeMap.put(key, System.currentTimeMillis() + GLOBAL_TIMEOUT);

		return storageMap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		long l = System.currentTimeMillis();

		m.entrySet().forEach(en -> {
			if (GLOBAL_TIMEOUT != -1)
				timeMap.put(en.getKey(), l + GLOBAL_TIMEOUT);

			storageMap.put(en.getKey(), en.getValue());
		});
	}

	public void putAll(Map<? extends K, ? extends V> m, long timeOut) {
		long l = System.currentTimeMillis();

		m.entrySet().forEach(en -> {
			timeMap.put(en.getKey(), l + timeOut);
			storageMap.put(en.getKey(), en.getValue());
		});
	}

	@Override
	public void clear() {
		timeMap.clear();
		storageMap.clear();
	}

	@Override
	public Set<K> keySet() {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.keySet();
	}

	@Override
	public Collection<V> values() {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.entrySet();
	}

	/**
	 * 获取大小
	 *
	 * @return
	 */
	@Override
	public int size() {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.size();
	}

	/**
	 * 判断集合是否为空
	 *
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.isEmpty();
	}

	/**
	 * 删除数据
	 *
	 * @param key
	 * @return
	 */
	@Override
	public V remove(Object key) {
		// 如果当前的key已经过期则进行删除
		@SuppressWarnings("unchecked")
		boolean b = delPastKey((K) key);
		// 返回true,代表当前数据已过期，并已清除
		if (b)
			return null;

		timeMap.remove(key);
		return storageMap.remove(key);
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsKey(Object key) {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.containsKey(key);
	}

	/**
	 * 判断value是否存在
	 *
	 * @param value
	 * @return
	 */
	@Override
	public boolean containsValue(Object value) {
		// 清除所有过期的数据
		delPastKeyAll();
		return storageMap.containsValue(value);
	}

	/**
	 * 删除过期的数据
	 *
	 * @param key
	 */
	private boolean delPastKey(K key) {
		boolean flag = false;
		// 当前时间
		long l = System.currentTimeMillis();
		Long v = timeMap.get(key);
		if (v != null && l > v) {
			storageMap.remove(key);
			timeMap.remove(key);
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除所有过期的数据
	 */
	private void delPastKeyAll() {
		// 当前时间
		long l = System.currentTimeMillis();
		Set<Entry<K, Long>> entries = timeMap.entrySet();

		for (Entry<K, Long> kLongEntry : entries) {
			Long v = kLongEntry.getValue();

			if (l > v) {
				storageMap.remove(kLongEntry.getKey());
				timeMap.remove(kLongEntry.getKey());
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		CacheMap<String, String> map = new CacheMap<>();
		map.put("1", "1", 3);
		map.put("2", "2", 2);
		map.put("3", "3");

		System.out.println("超时前1-》：" + map.get("1"));
		System.out.println("超时前2-》：" + map.get("2"));
		System.out.println("超时3-》：" + map.get("3"));
		System.err.println("超时前长度-》：" + map.size());
		Thread.sleep(4000);
		System.err.println("超时后长度-》：" + map.size());
		System.out.println("超时后1-》：" + map.get("1"));
		System.out.println("超时后2-》：" + map.get("2"));
		System.out.println("超时后3-》：" + map.get("3"));

		Thread.sleep(3000);

		System.out.println("整个map超时后3:-》：" + map.get("3"));

//        Thread.sleep(2000);
//        //当前获取到的全是没有超时的，但是在使用这个Entry时，有数据超时了，这部分超时数据，不负责处理
//        //entrySet相当于获取的是当前map集合的快照
//        Set<Entry<String, String>> entries = map.entrySet();
//
//        map.remove("1");
//
//        for (Entry<String, String> entry : entries) {
//            System.err.println(entry.getKey());
//        }

	}
}
