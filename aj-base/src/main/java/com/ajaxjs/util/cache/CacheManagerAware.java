package com.ajaxjs.util.cache;

/**
 * 说明这是缓存感知的组件。需要设置 Cache 管理器
 */
public interface CacheManagerAware {
	/**
	 * 设置置 Cache 管理器
	 * 
	 * @param cacheManager Cache 管理器
	 */
	void setCacheManager(CacheManager cacheManager);
}
