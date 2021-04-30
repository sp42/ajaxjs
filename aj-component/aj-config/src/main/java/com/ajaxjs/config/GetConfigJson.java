package com.ajaxjs.config;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 以 JSON 为存储格式的配置系统，在 JVM 中以 Map/List 结构保存 该类是单例。
 * 
 * @author frank
 *
 */
public class GetConfigJson implements GetConfig {
	private static final LogHelper LOGGER = LogHelper.getLog(GetConfigJson.class);

	/**
	 * 
	 */
	private ConfigService cfgService;

	/**
	 * 内部的获取方法
	 * 
	 * @param <T>         配置类型
	 * @param key         配置键值
	 * @param isNullValue 当配置为 null 时返回的值，相当于“默认值”
	 * @param vType       配置类型的引用
	 * @return 配置内容
	 */
	@SuppressWarnings("unchecked")
	private <T> T get(String key, T isNullValue, Class<T> vType) {
		if (!cfgService.isLoaded() || cfgService.getFlatConfig() == null)
			return isNullValue;

		Object v = cfgService.getFlatConfig().get(key);

		if (v == null) {
			LOGGER.warning("没发现[{0}]配置", key);
			return isNullValue;
		}

		return (T) v;
	}

	@Override
	public String get(String key) {
		return get(key, null, String.class);
	}

	@Override
	public boolean getBol(String key) {
		return get(key, false, boolean.class);
	}

	@Override
	public int getInt(String key) {
		return get(key, 0, int.class);
	}

	@Override
	public long getLong(String key) {
		return get(key, 0L, long.class);
	}

}
