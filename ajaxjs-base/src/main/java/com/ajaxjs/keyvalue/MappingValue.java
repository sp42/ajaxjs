/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.keyvalue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ajaxjs.util.CommonUtil;

/**
 * 处理值的一些相关函数
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class MappingValue {

	/**
	 * 强类型转换，有 null 检测
	 * 
	 * @param obj   输入的对象
	 * @param clazz 目标类型
	 * @return T型结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T TypeConvert(Object obj, Class<T> clazz) {
		return obj == null ? null : (T) obj;
	}

	/**
	 * 把字符串还原为 Java 里面的真实值，如 "true"--true,"123"--123,"null"--null
	 * 
	 * @param value 字符串的值
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
		if (value.charAt(0) == '-' || (value.charAt(0) >= '0' && value.charAt(0) <= '9'))
			try {
				int int_value = Integer.parseInt(value);
				if ((int_value + "").equals(value)) // 判断为整形
					return int_value;
			} catch (NumberFormatException e) {// 不能转换为数字
			}

		return value;
	}

	/**
	 * true/1、 字符串 true／1/yes/on 被视为 true 返回； false／0/null、字符串 false／0/no/off/null
	 * 被视为 false 返回；
	 * 
	 * @param value 输入值
	 * @return true/false
	 */
	public static boolean toBoolean(Object value) {
		if (value == null)
			return false;

		if (value.equals(true) || value.equals(1))
			return true;

		if (value instanceof String) {
			String _value = (String) value;
			if (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("true") || _value.equals("1")
					|| _value.equalsIgnoreCase("on"))
				return true;

			if (_value.equalsIgnoreCase("no") || _value.equalsIgnoreCase("false") || _value.equals("0")
					|| _value.equalsIgnoreCase("off") || _value.equalsIgnoreCase("null"))
				return false;
		}

		return false;
	}

	/**
	 * 根据送入的类型作适当转换
	 * 
	 * @param value 送入的值
	 * @param t     期待的类型
	 * @return 已经转换类型的值
	 */
	@SuppressWarnings("unchecked")
	public static Object objectCast(Object value, Class<?> t) {
		if (value == null)
			return null;
		else if (t == boolean.class || t == Boolean.class) {// 布尔型
			value = toBoolean(value);
		} else if (t == int.class || t == Integer.class) { // 整形
			value = Integer.parseInt(value.toString());
		} else if (t == int[].class || t == Integer[].class) {
			// 复数
			if (value instanceof String) {
				value = stringArr2intArr((String) value, diver + "");
			} else if (value instanceof List) {
				value = integerList2arr((List<Integer>) value);
			}

		} else if (t == long.class || t == Long.class) {
			// LONG 型
			value = Long.valueOf(value.toString());
		} else if (t == String.class) { // 字符串型
			value = value.toString();
		} else if (t == String[].class) {
			// 复数
			if (value instanceof ArrayList) {
				value = stringList2arr((ArrayList<String>) value);
			} else if (value instanceof String) {
				String str = (String) value;
				value = str.split(diver + "");
			} else {
				// LOGGER.info("Bean 要求 String[] 类型，但实际传入类型：" +
				// value.getClass().getName());
			}
		} else if (t == Date.class) {
			value = CommonUtil.Objet2Date(value);
		} else if (t == BigDecimal.class) {
			if (value instanceof Integer) {
				value = new BigDecimal((Integer) value);
			} else if (value instanceof String) {
				BigDecimal b = new BigDecimal((String) value);
				b.setScale(2, BigDecimal.ROUND_DOWN);

				value = b;
			}
		}

		return value;
	}

	/**
	 * 用于数组的分隔符
	 */
	static char diver = ',';

	/**
	 * Integer[] 不能直接转 int[]
	 * 
	 * @param list 整形列表
	 * @return 整形数组
	 */
	private static int[] integerList2arr(List<Integer> list) {
		int[] arr = new int[list.size()];

		for (int i = 0; i < list.size(); i++)
			arr[i] = list.get(i);

		return arr;
	}

	/**
	 * 当它们每一个都是数字的字符串形式，转换为整形的数组 "1,2,3, ..." -- [1, 2, ...]
	 * 
	 * @param value 输入字符串
	 * @param diver 分隔符
	 * @return 整形数组
	 */
	private static int[] stringArr2intArr(String value, String diver) {
		String[] strArr = value.split(diver);
		int[] intArr = new int[strArr.length];

		for (int i = 0; i < strArr.length; i++)
			intArr[i] = Integer.parseInt(strArr[i].trim());

		return intArr;
	}

	/**
	 * List&lt;String&gt; 转换为字符串数组／数组效的话率更高一些
	 * 
	 * @param list 字符串列表
	 * @return 字符串数组
	 */
	private static String[] stringList2arr(List<String> list) {
		return list.toArray(new String[list.size()]);
	}
}
