package com.ajaxjs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapTool {
	/**
	 * Map 转换为 String
	 * 
	 * @param map Map
	 * @return String
	 */
	public static <T> String join(Map<String, T> map, String div, Function<T, String> fn) {
		String[] pairs = new String[map.size()];

		int i = 0;

		for (String key : map.keySet())
			pairs[i++] = key + "=" + fn.apply(map.get(key));

		return String.join(div, pairs);
	}

	public static <T> String join(Map<String, T> map, Function<T, String> fn) {
		return join(map, "&", fn);
	}

	public static <T> String join(Map<String, T> map, String div) {
		return join(map, div, v -> v == null ? null : v.toString());
	}

	public static <T> String join(Map<String, T> map) {
		return join(map, v -> v == null ? null : v.toString());
	}

	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param pairs 结对的字符串数组
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] pairs, Function<String, Object> fn) {
		if (CommonUtil.isNull(pairs))
			return null;

		Map<String, Object> map = new HashMap<>();

		for (String pair : pairs) {
			if (!pair.contains("="))
				throw new IllegalArgumentException("没有 = 不能转化为 map");

			String[] column = pair.split("=");

			if (column.length >= 2)
				map.put(column[0], fn == null ? column[1] : fn.apply(column[1]));
			else
				map.put(column[0], "");// 没有 等号后面的，那设为空字符串
		}

		return map;
	}

	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param columns 结对的键数组
	 * @param values 结对的值数组
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] columns, String[] values, Function<String, Object> fn) {
		if (CommonUtil.isNull(columns))
			return null;

		if (columns.length != values.length)
			throw new UnsupportedOperationException("两个数组 size 不一样");

		Map<String, Object> map = new HashMap<>();

		int i = 0;
		for (String column : columns)
			map.put(column, fn.apply(values[i++]));

		return map;
	}

	/**
	 * 判断 map 非空，然后根据 key 获取 value，若 value 非空则作为参数传入函数接口 s
	 * 
	 * @param map
	 * @param key
	 * @param s
	 */
	public static <T> void getValue(Map<String, T> map, String key, Consumer<T> s) {
		if (map != null) {
			T value = map.get(key);
			if (value != null)
				s.accept(value);
		}
	}

	public static <T, K> Map<String, T> as(Map<String, K> map, Function<K, T> fn) {
		Map<String, T> _map = new HashMap<>();

		for (String key : map.keySet()) {
			K value = map.get(key);
			_map.put(key.toString(), value == null ? null : fn.apply(value));
		}

		return _map;
	}
}
