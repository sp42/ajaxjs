/**
 * Copyright Frank Cheung frank@ajaxjs.com
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.collection.CollectionUtil;

/**
 * 处理值的一些相关函数
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class Value {

	/**
	 * 强类型转换，有 null 检测
	 * 
	 * @param obj
	 *            输入的对象
	 * @param clazz
	 *            目标类型
	 * @return T型结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T TypeConvert(Object obj, Class<T> clazz) {
		return obj == null ? null : (T) obj;
	}

	/**
	 * 强类型转换为字符串型
	 * 
	 * @param obj
	 *            输入的对象
	 * @param isUserToString
	 *            是否使用 toString() 转换
	 * @return 字符串型
	 */
	public static String to_String(Object obj, boolean isUserToString) {
		if (obj != null) {
			return isUserToString ? obj.toString() : TypeConvert(obj, String.class);
		} else
			return null;
	}

	/**
	 * 强类型转换为字符串型（使用强类型转换而不是 toString()）
	 * 
	 * @param obj
	 *            任意对象
	 * @return 字符串型
	 */
	public static String to_String(Object obj) {
		return to_String(obj, false);
	}

	/**
	 * 从字符串还原真实值， 空字符串"" 会被视为 null 获取参数，并自动从字符串转换为 Java 类型，如 "true"-->
	 * true,"123"--> 123,"null"-->null
	 * 
	 * @param value
	 *            字符串的值
	 * @return Java 里面的值
	 */
	public static Object toJavaValue(String value) {
		if (value == null)
			return null;

		value = value.trim();

		if ("null".equals(value))
			return null;

		if ("true".equalsIgnoreCase(value))
			return true;
		if ("false".equalsIgnoreCase(value))
			return false;

		// try 比较耗资源，先检查一下
		if (value.charAt(0) >= '0' && value.charAt(0) <= '9')
			try {
				int int_value = Integer.parseInt(value);
				if ((int_value + "").equals(value)) // 判断为整形
					return int_value;
			} catch (NumberFormatException e) {// 不能转换为数字
			}

		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean toBoolean(Object value) {
		if (value.equals(true) || value.equals(1))
			return true;

		if (value instanceof String) {
			String _value = (String) value;
			if (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("true") || _value.equals("1")
					|| _value.equalsIgnoreCase("on"))
				return true;

			if (_value.equalsIgnoreCase("no") || _value.equalsIgnoreCase("false") || _value.equals("0")
					|| _value.equalsIgnoreCase("off"))
				return false;
		}

		return false;
	}

	/**
	 * 
	 * @param value
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object objectCast(Object value, Class<?> t) {
		if (t == boolean.class || t == Boolean.class) {// 布尔型
			value = toBoolean(value);
		} else if (t == int.class || t == Integer.class) { // 整形
			value = Integer.parseInt(value.toString());
		} else if (t == int[].class || t == Integer[].class) {
			// 复数
			if (value instanceof String) {
				value = CollectionUtil.strArr2intArr((String) value, diver + "");
			} else if (value instanceof List) {
				value = CollectionUtil.integerList2arr((List<Integer>) value);
			}

		} else if (t == long.class || t == Long.class) {
			// LONG 型
			value = Long.valueOf(value.toString());
		} else if (t == String.class) { // 字符串型
			value = value.toString();
		} else if (t == String[].class) {
			// 复数
			if (value instanceof ArrayList) {
				value = CollectionUtil.stringList2arr((ArrayList<String>) value);
			} else if (value instanceof String) {
				String str = (String) value;
				value = str.split(diver + "");
			} else {
				// LOGGER.info("Bean 要求 String[] 类型，但实际传入类型：" +
				// value.getClass().getName());
			}
		} else if (t == Date.class) {
			value = DateTools.Objet2Date(value);
		}

		return value;
	}

	/**
	 * 用于数组的分隔符
	 */
	static char diver = ',';

	/**
	 * 输出符合 JSON 要求的值
	 * 
	 * @param value
	 *            任意一个 Java 值
	 * @return JSON 形式的值
	 */
	@SuppressWarnings("unchecked")
	public static String obj2jsonVaule(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof Double) {
			return double2int((Double) value) + "";
		} else if (value instanceof Boolean || value instanceof Number) {
			return value.toString();
		} else if (value instanceof Date) {
			return '\"' + DateTools.formatDate((Date) value, DateTools.commonDateFormat) + '\"';
		} else if (value instanceof Map) {
			return JsonHelper.stringifyMap((Map<String, ?>) value);
		} else if (value instanceof List) {
			List<?> list = (List<?>) value;

			if (list.size() == 0) {
				return "[]";
			} else if (list.get(0) instanceof String) {
				List<String> strList = (List<String>) list;
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < strList.size(); i++) {
					if (i == (strList.size() - 1))
						sb.append("\"" + strList.get(i) + "\"");
					else
						sb.append("\"" + strList.get(i) + "\"").append(",");
				}

				return '[' + sb.toString() + ']';
			} else if (list.get(0) instanceof Map) {
				List<String> jsonStrList = new ArrayList<>();
				List<Map<String, ?>> maps = (List<Map<String, ?>>) list;

				for (Map<String, ?> map : maps)
					jsonStrList.add(JsonHelper.stringifyMap(map));

				return obj2jsonVaule(jsonStrList);
			} else {
				// 未知类型数组，
				return "[]";
			}

			// return stringify((Map<String, ?>)value);
		} else if (value.getClass() == int[].class) {
			String[] strs = CollectionUtil.int_arr2string_arr((int[]) value);
			return '[' + StringUtil.stringJoin(strs) + ']';
		} else if (value instanceof Object[]) {
			Object[] arr = (Object[]) value;
			String[] strs = new String[arr.length];
			int i = 0;

			for (Object _value : arr)
				strs[i++] = obj2jsonVaule(_value); // 递归调用

			return '[' + StringUtil.stringJoin(strs) + ']';
		} else { // String
			return '\"' + value.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
					.replace("\r", "\\r") + '\"';
		}
	}

	/**
	 * js number 为 double 类型，在 java 里面使用不方便，将其转换为 int
	 * 
	 * @param d
	 *            js number
	 * @return int 值
	 */
	public static int double2int(Double d) {
		if (d > Integer.MAX_VALUE) {
			// LOGGER.warning("数值 {}0 太大，不应用这个方法转换到 int", d);
			return 0;
		} else {
			return d.intValue();
		}
	}
}
