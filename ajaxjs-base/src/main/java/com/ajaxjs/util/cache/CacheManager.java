package com.ajaxjs.util.cache;

/**
 * 缓存管理器接口，该接口提供具体的cache实现
 * 
 */
public interface CacheManager {
	/**
	 * 根据 cache 的名称获取 cache。如果不存在，默认新建并返回
	 * 
	 * @param name Cache 的名称
	 * @return Cache
	 */
	public <K, V> Cache<K, V> getCache(String name);
	
	public <V> Cache<String, V> getCache(String name, Class<V> clz);

	/**
	 * 销毁cache
	 */
	public void destroy();
}
