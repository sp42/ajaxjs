/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.map;

import java.util.Map;

/**
 * 链式调用处理 map 数据
 * @author sp42
 *
 */
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
	 * String[]-->String
	 * @return
	 */
	public MapData toMap() {
		parameterMap_String = MapHelper.toMap(getParameterMapRaw());
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
	public MapData setParameterMapRaw(Map<String, String[]> parameterMapRaw) {
		this.parameterMapRaw = parameterMapRaw;
		return this;
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
	public MapData setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
		return this;
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
	public MapData setParameterMap_String(Map<String, String> parameterMap_String) {
		this.parameterMap_String = parameterMap_String;
		return this;
	}
}
