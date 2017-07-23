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
package com.ajaxjs.util.collection;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Value;

/**
 * 自定义 Map 类，加入更多实用的功能
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class MapHelper {
	/**
	 * 也是 join，不过输入的参数不是数组而是 hash。
	 * 
	 * @param map
	 *            输入的 map
	 * @param div
	 *            分隔符
	 * @return 连续的字符串
	 */
	public static String join(Map<String, String> map, String div) {
		String[] pairs = new String[map.size()];

		int i = 0;
		for (String key : map.keySet())
			pairs[i++] = key + "=" + map.get(key);

		/*
		 * 另外一种算法 // for (String key : pair.keySet()) cookieStr += key + "=" +
		 * pair.get(key) + ";"; // cookieStr = cookieStr.substring(0,
		 * cookieStr.length() - 1); // 删掉最后一个分号 // 另外一种算法 int i = 0; for(String
		 * key : hash.keySet()){ ... if(++i != size)buff.append(","); } //
		 * 另外一种算法，删除最后一个 , if (buff.length() > 1)buff =
		 * buff.deleteCharAt(buff.length() - 1); // 另外一种算法，删除最后一个 , ... if(i !=
		 * arr.length - 1)str += ",";
		 */
		return StringUtil.stringJoin(pairs, div);
	}

	/**
	 * Map 转换为字符串，默认 & 作为分隔符
	 * 
	 * @param map
	 *            输入的 map
	 * @return 连续的字符串
	 */
	public static String join(Map<String, String> map) {
		return join(map, "&");
	}

	/**
	 * Map 的泛型类型转换，转换为 Map<String, Object>。 可选择类型转换真实值，如
	 * "2"-->2，"true",-->true，"null"-->null。要求 String 类型才可以 CastRealValue
	 * 
	 * @param map
	 * @param isCastRealValue
	 *            true 表示为转换类型
	 * @return Map<String, Object> 结构
	 */
	public static Map<String, Object> asObject(Map<String, ?> map, boolean isCastRealValue) {
		if(map == null) return null;
		
		Map<String, Object> as = new HashMap<>();

		for (String key : map.keySet()) {
			Object obj = map.get(key);
			boolean toCast = isCastRealValue && obj != null && obj instanceof String;
			
			as.put(key, toCast ? Value.toJavaValue(obj.toString()) : obj);
		}

		return as;
	}

	/**
	 * Map 的泛型类型转换，从 Map<String, ?> 转换为 Map<String, Object>
	 * 
	 * @param map
	 *            Map<String, ?> 结构
	 * @return Map<String, Object> 结构
	 */
	public static Map<String, Object> asObject(Map<String, ?> map) {
		return asObject(map, false);
	}

	/**
	 * Map 的泛型类型转换，从 Map<String, ?> 转换为 Map<String, String>
	 * 
	 * @param map
	 *            Map<String, ?> 结构
	 * @return Map<String, String> 结构
	 */
	public static Map<String, String> asString(Map<String, ?> map) {
		if(map == null) return null;
		
		Map<String, String> as = new HashMap<>();

		for (String key : map.keySet())
			as.put(key, Value.to_String(map.get(key), true));

		return as;
	}
	
	/**
	 * Map<String, String[]> 转换为 Map<String, String>，其中 value[] 变成 , 分割的单个字符串。
	 * 
	 * @param map
	 *            输入的 Map
	 * @return 以 , 分割的单个字符串
	 */
	public static Map<String, String> toMap(Map<String, String[]> map) {
		if(map == null) return null;
		
		Map<String, String> _map = new HashMap<>();
		
		for (String key : map.keySet()) {
			String[] values = map.get(key);
			String value = values.length == 1 ?  values[0] : StringUtil.stringJoin(values, ",");
			
			_map.put(key, value);
		}
		
		return _map;
	}

	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param columns
	 *            结对的键数组
	 * @param values
	 *            结对的值数组
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] columns, String[] values) {
		if (CollectionUtil.isNotNull(columns)) 
			return null;
		
		if (columns.length != values.length)
			throw new UnsupportedOperationException("两个数组 size 不一样");
		
		Map<String, Object> map = new HashMap<>();

		int i = 0;
		for (String column : columns) {
			map.put(column, Value.toJavaValue(values[i]));
			i++;
		}

		return map;
	}

	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param pairs
	 *            结对的字符串数组
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] pairs) {
		return toMap(pairs, false);
	}
	
	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param pairs
	 *            结对的字符串数组
	 * @param isDecode
	 *            是否 url 解码
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] pairs, boolean isDecode) {
		if (!CollectionUtil.isNotNull(pairs))
			return null;
		
		Map<String, Object> map = new HashMap<>();
		
		for (String pair : pairs) {
			if (!pair.contains("=")) {
				throw new IllegalArgumentException("没有 = 不能转化为 map");
			}
			
			String[] column = pair.split("=");
			
			if (column.length >= 2)
				map.put(column[0], Value.toJavaValue((isDecode ? StringUtil.urlDecode(column[1]) : column[1])));
			else
				map.put(column[0], "");// 没有 等号后面的，那设为空字符串
		}
		
		return map;
	}

	/**
	 * 免去强类型转换的麻烦
	 * 
	 * @param map
	 *            Map<String, ?> map
	 * @param key
	 *            键
	 * @return 布尔型
	 */
	public static boolean getBoolean(Map<String, ?> map, String key) {
		if (map == null)
			return false;
		else {
			Object obj = map.get(key);
			return obj == null ? false : (boolean) obj;
		}
	}

	/**
	 * 有些对象实现了 Map 接口但只能读不能写，现在为真正的 Hashmap
	 * 
	 * @param map
	 *            符合 Map 接口的伪 Map
	 * @return 真正的 Hashmap
	 */
	public static Map<String, Object> toRealMap(Map<String, ?> map) {
		if (map == null)
			return null;
		
		Map<String, Object> _map = new HashMap<>();
		for (String key : map.keySet()) {
			_map.put(key, map.get(key));
		}

		return _map;
	}
}
