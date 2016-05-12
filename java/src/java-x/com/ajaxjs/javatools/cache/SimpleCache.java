package com.ajaxjs.javatools.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地 cache 工具
 * @singleton 单例
 */
public abstract class SimpleCache{
	// 为什么要用 ConcurrentHashMap？详见  http://cuisuqiang.iteye.com/blog/1564044
	private static Map<String, Object> map = new ConcurrentHashMap<>();
	
	/**
	 * 清除缓存
	 */
	public static void clear() {
		map.clear();
		map = null;
	}
	
	public static Map<String, Object> getCache() {
		return map;
	}

	public static void set(String key, Object values) {
		map.put(key, values);
	}

	public static Object get(String key) {
		return map.get(key);
	}

	public static String getString(String key) {
		return (String) map.get(key);
	}

	public static Object getToEmpty(String key) {
		Object o = map.get(key);
		if (o == null)
			return "";
		else
			return o;
	}

	public static void remove(String key) {
		map.remove(key);
	}

	public static void main(String[] args) {
		SimpleCache.set("001", "001");
		SimpleCache.set("002", "002");
		SimpleCache.set("003", "003");

		System.out.println(SimpleCache.get("001"));
		System.out.println(SimpleCache.get("002"));
		System.out.println(SimpleCache.get("003"));
	}
}
