package com.ajaxjs.fast_doc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Util {
	/**
	 * 对一个数组进行迭代，返回一个 list
	 * 
	 * @param <T>
	 * @param arr
	 * @param fn
	 * @return
	 */
	public static <T, K> List<T> makeListByArray(K[] arr, Function<K, T> fn) {
		List<T> values = new ArrayList<>();

		for (K obj : arr) {
			T v = fn.apply(obj);
			values.add(v);
		}

		return values;
	}

	static boolean isSimpleValueType(Class<?> type) {
		return type.isPrimitive() || type == Boolean.class || type == Integer.class || type == Long.class || type == String.class
				|| type == Double.class || type == Float.class;
	}

	/**
	 * 类全称转为 .java 磁盘路径
	 * 
	 * @param clzName
	 * @return
	 */
	public static String className2JavaFileName(String clzName) {
		return clzName.replaceAll("\\.", "\\\\") + ".java";
	}

	public static String className2JavaFileName(Class<?> clz) {
		return className2JavaFileName(clz.getName());
	}
}
