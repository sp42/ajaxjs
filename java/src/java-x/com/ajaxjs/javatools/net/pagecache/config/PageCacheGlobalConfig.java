package com.ajaxjs.util.pagecache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局参数
 *
 */
public class PageCacheGlobalConfig {
	private static final long defaultExpiredTime = 1 * 24 * 60 * 60;				// 默认过时时间
	private static List<String> urlPattern = new ArrayList<>();						// 需要缓存的页面url的正则表达式列表，多个不同url的正则用，分隔
	private static Map<String, Integer> urlCacheTime = new HashMap<>();				// 对应urlPattern的每个url的缓存时间，单位秒，用，分隔
	private static Map<String, List<String>> urlIncludeParams = new HashMap<>();	// 要的参数
	private static Map<String, List<String>> urlExcludeParams = new HashMap<>();	// 不要的参数

	public static List<String> getUrlPattern() {
		return urlPattern;
	}

	public static void setUrlPattern(List<String> urlPattern) {
		PageCacheGlobalConfig.urlPattern = urlPattern;
	}

	public static long getCacheExpiredTime(String urlKey) {
		if (PageCacheGlobalConfig.getUrlCacheTime().containsKey(urlKey))
			return PageCacheGlobalConfig.getUrlCacheTime().get(urlKey);
		return defaultExpiredTime;
	}

	public static long getDefaultCacheExpiredTime() {
		return defaultExpiredTime;
	}

	public static Map<String, Integer> getUrlCacheTime() {
		return urlCacheTime;
	}

	public static void setUrlCacheTime(Map<String, Integer> urlCacheTime) {
		if (urlCacheTime == null) return;
		PageCacheGlobalConfig.urlCacheTime = urlCacheTime;
	}

	public static Map<String, List<String>> getUrlIncludeParams() {
		return urlIncludeParams;
	}

	public static void setUrlIncludeParams(Map<String, List<String>> urlIncludeParams) {
		if (urlIncludeParams == null) return;
		PageCacheGlobalConfig.urlIncludeParams = urlIncludeParams;
	}

	public static Map<String, List<String>> getUrlExcludeParams() {
		return urlExcludeParams;
	}

	public static void setUrlExcludeParams(Map<String, List<String>> urlExcludeParams) {
		if (urlExcludeParams == null) return;
		PageCacheGlobalConfig.urlExcludeParams = urlExcludeParams;
	}
}
