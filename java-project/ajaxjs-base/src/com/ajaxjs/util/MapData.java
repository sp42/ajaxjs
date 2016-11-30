package com.ajaxjs.util;

import java.util.Map;

import com.ajaxjs.util.MapHelper;

public class MapData {
	/**
	 * Servlet 返回的数据
	 */
	private Map<String, String[]> parameterMapRaw;

	/**
	 * Map 数据<String, Object>
	 */
	private Map<String, Object> parameterMap;

	/**
	 * Map 数据<String, String>
	 */
	private Map<String, String> parameterMap_String;

	/**
	 * 剔除不要的字段
	 * 
	 * @param ignoreField
	 * @return
	 */
	public MapData ignoreField(String ignoreField) {
		if (parameterMapRaw.containsKey(ignoreField))
			parameterMapRaw.remove(ignoreField);
		if (parameterMap.containsKey(ignoreField))
			parameterMap.remove(ignoreField);
		if (parameterMap_String.containsKey(ignoreField))
			parameterMap_String.remove(ignoreField);

		return this;
	}

	/**
	 * String 变为真实的 Java 数据 toDataAsObject().parameterMap();
	 * 
	 * @return
	 */
	public MapData toDataAsObject() {
		parameterMap = MapHelper.asObject(getParameterMap_String(), true);
		return this;
	}

	/**
	 * @return the parameterMapRaw
	 */
	public Map<String, String[]> getParameterMapRaw() {
		return parameterMapRaw;
	}

	/**
	 * @param parameterMapRaw
	 *            the parameterMapRaw to set
	 */
	public void setParameterMapRaw(Map<String, String[]> parameterMapRaw) {
		this.parameterMapRaw = parameterMapRaw;
	}

	/**
	 * @return the parameterMap
	 */
	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	/**
	 * @param parameterMap
	 *            the parameterMap to set
	 */
	public void setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	/**
	 * @return the parameterMap_String
	 */
	public Map<String, String> getParameterMap_String() {
		return parameterMap_String;
	}

	/**
	 * @param parameterMap_String
	 *            the parameterMap_String to set
	 */
	public void setParameterMap_String(Map<String, String> parameterMap_String) {
		this.parameterMap_String = parameterMap_String;
	}
}
