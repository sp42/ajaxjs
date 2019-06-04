package com.ajaxjs.javatools.net.pagecache.cache;

import java.util.Map;

public interface CacheStore {
	public void init(Map<String, String> initParams);

	/**
	 * 加入缓存
	 * 
	 * @param key
	 * @param value
	 * @param urlPattern
	 *            URL 正则
	 */
	public void put(String key, String value, String urlPattern);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);
}
