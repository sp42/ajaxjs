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
package com.ajaxjs.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.util.StringUtils;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.sql.util.geo.LocationPoint;
import com.ajaxjs.sql.util.geo.MySqlGeoUtils;
import com.ajaxjs.util.date.DateUtil;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 处理值的一些相关函数
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class MappingValue {
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

		if ("".equals(value))
			return "";
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
				try {
					long long_value = Long.parseLong(value);
					if ((long_value + "").equals(value)) // 判断为整形
						return long_value;
				} catch (NumberFormatException e1) {
					if (value.matches("[0-9]{1,13}(\\.[0-9]*)?"))
						return Double.parseDouble(value);
				}
			}

		return value;
	}

	/**
	 * true/1、 字符串 true/1/yes/on 被视为 true 返回； false/0/null、字符串 false/0/no/off/null
	 * 被视为 false 返回；
	 * 
	 * @param value 输入值
	 * @return true/false
	 */
	public static boolean toBoolean(Object value) {
		if (value == null)
			return false;

		if (value.equals(true) || value.equals(1) || value.equals(1L))
			return true;

		if (value instanceof String) {
			String _value = (String) value;

			if (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("true") || _value.equals("1") || _value.equalsIgnoreCase("on"))
				return true;

			if (_value.equalsIgnoreCase("no") || _value.equalsIgnoreCase("false") || _value.equals("0") || _value.equalsIgnoreCase("off")
					|| _value.equalsIgnoreCase("null"))
				return false;
		}

		return false;
	}

	/**
	 * 转换为 int 类型
	 * 
	 * @param value 送入的值
	 * @return int 类型的值
	 */
	public static int object2int(Object value) {
		if (value == null)
			return 0;

		if (value instanceof String)
			return Integer.parseInt((String) value);
		else if (value instanceof Number)
			return ((Number) value).intValue();
		else {
			String toString = value.toString();

			if (toString.matches("-?\\d+"))
				return Integer.parseInt(toString);

			throw new IllegalArgumentException("This Object doesn't represent an int");
		}
	}

	/**
	 * 转换为 long 类型
	 * 
	 * @param value 送入的值
	 * @return long 类型的值
	 */
	public static long object2long(Object value) {
		if (value == null)
			return 0;

		if (value instanceof Long)
			return (long) value;
		else if (value instanceof BigInteger)
			return ((BigInteger) value).longValue();
		else if (value instanceof Number)
			return ((Number) value).longValue();
		else if (value instanceof String)
			return Long.parseLong((String) value);

		throw new IllegalArgumentException("This Object doesn't represent an long");
	}

	/**
	 * 根据送入的类型作适当转换
	 * 
	 * @param value  送入的值
	 * @param target 期待的类型
	 * @return 已经转换类型的值
	 */
	@SuppressWarnings("unchecked")
	public static Object objectCast(Object value, Class<?> target) {
		if (value == null)
			return null;
		else if (target == boolean.class || target == Boolean.class) // 布尔型
			value = toBoolean(value);
		else if (target == int.class || target == Integer.class) { // 整形
			if (value instanceof Integer) {

//				System.out.println("::::::" + value.getClass());
//				System.out.println("::::::" + value.getClass().getName());

			} else
				value = StringUtils.hasText(value.toString()) ? Integer.parseInt(value.toString()) : 0;
		} else if (target == int[].class || target == Integer[].class) {
			// 复数
			if (value instanceof String)
				value = stringArr2intArr((String) value, DIVER + "");
			else if (value instanceof List)
				value = intList2Arr((List<Integer>) value);

		} else if (target == long.class || target == Long.class)
			// LONG 型
			value = Long.valueOf((value == null || StringUtils.hasText(value.toString())) ? value.toString() : "0");
		else if (target == String.class) // 字符串型
			value = value.toString();
		else if (target == String[].class) {
			// 复数
			if (value instanceof ArrayList) {
				ArrayList<String> list = (ArrayList<String>) value;
				value = list.toArray(new String[list.size()]);
			} else if (value instanceof String) {
				String str = (String) value;
				value = str.split(DIVER + "");
			} else {
				// LOGGER.info("Bean 要求 String[] 类型，但实际传入类型：" +
				// value.getClass().getName());
			}
		} else if (target == Date.class)
			value = DateUtil.object2Date(value);
		else if (target == BigDecimal.class) {
			if (value instanceof Integer)
				value = new BigDecimal((Integer) value);
			else if (value instanceof String) {
				BigDecimal b = new BigDecimal((String) value);
				b.setScale(2, BigDecimal.ROUND_DOWN);

				value = b;
			} else if (value instanceof Double)
				value = new BigDecimal(Double.toString((Double) value));
		} else if (target == Map.class) {
			if (value instanceof String) {
				value = JsonHelper.parseMap((String) value);
			}
		} else if (target == float.class || target == Float.class) {
			if (value instanceof Double) {
				value = ((Double) value).floatValue();
			}
		} else if (target == double.class || target == Double.class) {
//			if (value instanceof Float) {
//				value = ((Float) value).doubleValue();
//			}
			value = Double.parseDouble(value.toString());
		} else if (value instanceof String && IBaseModel.class.isAssignableFrom(target)) {
			// json2bean
			value = JsonHelper.parseMapAsBean((String) value, target);
		} else if (target.isEnum()) { // 枚举
			Object[] enumConstants = target.getEnumConstants();

			boolean isNumber = value instanceof Integer;
			for (Object obj : enumConstants) { // value 跟枚举类型比较
				if (isNumber) {
					Enum<?> e = ((Enum<?>) obj);

					if (e.ordinal() == ((Integer) value)) {
						value = e.ordinal();
						break;
					}

				} else if (obj.toString().equals(value)) {
					value = obj;
					break;
				}
			}
		} else if (target == LocationPoint.class) { // mysql Geo 信息
			if (value instanceof byte[]) {
				double[] point = MySqlGeoUtils.bytesToOnePoint((byte[]) value);
				LocationPoint p = new LocationPoint();
				p.setLatitude(point[0]);
				p.setLongitude(point[1]);

				value = p;
			} else if (value instanceof Map) {
				LocationPoint p = new LocationPoint();
				Map<String, Object> map = (Map<String, Object>) value;
				p.setLatitude(Double.parseDouble(map.get("latitude").toString()));
				p.setLongitude(Double.parseDouble(map.get("longitude").toString()));

				value = p;
			}
		}

		return value;
	}

	/**
	 * 用于数组的分隔符
	 */
	private static final char DIVER = ',';

	/**
	 * Integer[] 不能直接转 int[]，故设置一个函数专门处理之
	 * 
	 * @param list 整形列表
	 * @return 整形数组
	 */
	private static int[] intList2Arr(List<Integer> list) {
		return newIntArray(list.size(), index -> list.get(index));
	}

	/**
	 * 当它们每一个都是数字的字符串形式，转换为整形的数组
	 * 
	 * <pre>
	 * {@code
	 *   "1,2,3, ..." -- [1, 2, ...]
	 * }
	 * </pre>
	 * 
	 * @param value 输入字符串
	 * @param diver 分隔符
	 * @return 整形数组
	 */
	private static int[] stringArr2intArr(String value, String diver) {
		String[] strArr = value.split(diver);

		return newIntArray(strArr.length, index -> Integer.parseInt(strArr[index].trim()));
	}

	/**
	 * 创建一个 int 数列，对其执行一些逻辑
	 * 
	 * @param length 数列最大值
	 * @param fn     执行的逻辑
	 * @return 数组
	 */
	private static int[] newIntArray(int length, Function<Integer, Integer> fn) {
		int[] arr = new int[length];

		for (int i = 0; i < length; i++)
			arr[i] = fn.apply(i);

		return arr;
	}
}
