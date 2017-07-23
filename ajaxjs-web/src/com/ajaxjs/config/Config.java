package com.ajaxjs.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置 Model
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class Config extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * json 文件路径
	 */
	private String jsonPath;
	
	/**
	 * json 字符串内容
	 */
	private String jsonStr;

	/**
	 * 是否加载成功
	 */
	private boolean isLoaded;


	/**
	 * @return the jsonPath
	 */
	public String getJsonPath() {
		return jsonPath;
	}

	/**
	 * @param jsonPath the jsonPath to set
	 */
	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	/**
	 * @return the isLoaded
	 */
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * @param isLoaded the isLoaded to set
	 */
	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * @return the jsonStr
	 */
	public String getJsonStr() {
		return jsonStr;
	}

	/**
	 * @param jsonStr the jsonStr to set
	 */
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
}
