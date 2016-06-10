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
package com.ajaxjs.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

/**
 * 自定义 Map 类，加入更多实用的功能
 * 
 * @author Frank Cheung
 */
public class MapHelper {
	/**
	 * 从字符串还原真实值， 空字符串"" 会被视为 null 获取参数，并自动从字符串转换为 Java 类型，如 "true"-->
	 * true,"123"--> 123,"null"-->null
	 * 
	 * @param value
	 *            字符串的值
	 * @return Java 里面的值
	 */
	public static Object toJavaValue(String value) {
		Object _return = value;

		if (value == null)
			return null;
		value = value.trim();

		if ("".equals(value) || "null".equals(value))
			return null;

		if ("true".equalsIgnoreCase(value))
			return true;
		if ("false".equalsIgnoreCase(value))
			return false;

		try {
			int int_value = Integer.parseInt(value);
			if ((int_value + "").equals(value)) // 判断为整形
				return int_value;
		} catch (NumberFormatException e) {// 不能转换为数字
			_return = value;
		}

		return _return;
	}

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
	 *            trun 表示为转换类型
	 * @return Map<String, Object> 结构
	 */
	public static Map<String, Object> asObject(Map<String, ?> map, boolean isCastRealValue) {
		Map<String, Object> as = null;

		if (map != null) {
			as = new HashMap<>();
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				boolean toCast = isCastRealValue && obj != null && obj instanceof String;
				as.put(key, toCast ? toJavaValue(obj.toString()) : obj);
			}
		}

		return as;
	}

	/**
	 * Map 的泛型类型转换，转换为 Map<String, Object>
	 * 
	 * @param map
	 *            Map<String, ?> 结构
	 * @return Map<String, Object> 结构
	 */
	public static Map<String, Object> asObject(Map<String, ?> map) {
		return asObject(map, false);
	}

	/**
	 * Map 的泛型类型转换，转换为 Map<String, String>
	 * 
	 * @param map
	 *            Map<String, ?> 结构
	 * @return Map<String, String> 结构
	 */
	public static Map<String, String> asString(Map<String, ?> map) {
		Map<String, String> as = null;

		if (map != null) {
			as = new HashMap<>();
			for (String key : map.keySet())
				as.put(key, Util.to_String(map.get(key), true));
		}

		return as;
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
		Map<String, Object> map = null;

		if (Util.isNotNull(columns)) {
			if (columns.length != values.length)
				throw new UnsupportedOperationException("两个数组 size 不一样");

			map = new HashMap<>();

			int i = 0;
			for (String column : columns) {
				map.put(column, toJavaValue(values[i]));
				i++;
			}
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
		Map<String, Object> map = null;

		if (Util.isNotNull(pairs)) {
			map = new HashMap<>();
			for (String pair : pairs) {
				if (!pair.contains("="))
					throw new IllegalArgumentException("没有 = 不能转化为 map");
				String[] column = pair.split("=");
				if (column.length >= 2)
					map.put(column[0], toJavaValue(column[1]));
				else
					// 没有 等号后面的，那设为空字符串
					map.put(column[0], "");
			}
		}

		return map;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> toMap(Map<String, String[]> map) {
		Map<String, String> _map = new HashMap<>();
		for (String key : map.keySet()) {
			String value;
			String[] values = map.get(key);
			if (values.length == 1) {
				value = values[0];
			} else {
				value = StringUtil.stringJoin(values, ",");
			}
			_map.put(key, value);
		}
		return _map;
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
		boolean b = false;
		if (map == null)
			b = false;
		else {
			Object obj = map.get(key);
			if (obj == null)
				b = false;
			else
				b = (boolean) obj;
		}

		return b;
	}

	/**
	 * 
	 * @param methodName
	 * @param action
	 *            set|get
	 * @return
	 */
	public static String getFieldName(String methodName, String action) {
		methodName = methodName.replace(action, "");
		methodName = Character.toString(methodName.charAt(0)).toLowerCase() + methodName.substring(1);
		return methodName;
	}

	public static Map<String, Object> setPojoToMapValue(Object obj) {
		if (obj != null) {
			Map<String, Object> map = new HashMap<>();

			for (Method method : obj.getClass().getMethods()) {
				String methodName = method.getName();
				if (methodName.startsWith("get")) {
					Object value = Reflect.executeMethod(obj, method);

					if (value != null) {
						map.put(getFieldName(methodName, "get"), value);
					}
				}
			}

			return map;
		} else {
			System.err.println("Null pointer");
			return null;
		}
	}

	/**
	 * 将 map 中数据填入到 obj 中。obj 是 POJO。 如果 POJO 有这个 setter，那就根据 setter 中 setXxx 获取
	 * xxx，作为 map 的 key 读取 map 的那個 value。
	 * 
	 * @param map
	 * @param obj
	 */
	public static void setMapValueToPojo(Map<String, Object> map, Object obj) {
		if (obj != null && map != null) {
			for (Method method : obj.getClass().getMethods()) {
				String methodName = method.getName();
				if (methodName.startsWith("set")) {
					methodName = getFieldName(methodName, "set");
					if (map.containsKey(methodName)) {
						Reflect.executeMethod(obj, method, map.get(methodName));
					}
				}
			}
		} else {
			System.err.println("Null pointer");
		}
	}
}
