package com.ajaxjs.config;

import java.util.Map;

/**
 * 配置数据
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Config {
	/**
	 * 配置文件路径
	 */
	private String filePath;

	/**
	 * 是否加载成功
	 */
	private boolean isLoaded;

	/**
	 * 所有的配置保存在这个 config 中
	 */
	private Map<String, Object> config;

	/**
	 * 所有的配置保存在这个 config 中（扁平化处理过的）
	 */
	private Map<String, Object> flatConfig;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	public Map<String, Object> getFlatConfig() {
		return flatConfig;
	}

	public void setFlatConfig(Map<String, Object> flatConfig) {
		this.flatConfig = flatConfig;
	}
}
