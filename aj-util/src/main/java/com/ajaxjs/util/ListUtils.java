package com.ajaxjs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 集合工具类
 * 
 * @author frank
 *
 */
public class ListUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(ListUtils.class);

	/**
	 * 即使 List 为空（null），也要返回一个空的 List
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> getList(List<T> list) {
		if (CollectionUtils.isEmpty(list))
			list = Collections.emptyList();

		return list;
	}

	/**
	 * 打印数组以便测试
	 * 
	 * @param arr
	 */
	public static void printArray(Object[] arr) {
		if (arr == null)
			LOGGER.debug("数组为空，null！");
		if (arr.length == 0)
			LOGGER.debug("数组不为空，但没有一个元素在内！");

		LOGGER.debug(Arrays.toString(arr));
	}

	/**
	 * 合并两个数组
	 * 
	 * @param <T>
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);

		return result;
	}

	/**
	 * 数组转换为 List
	 * 
	 * @param <E>
	 * @param elements
	 * @return
	 */
	@SafeVarargs
	public static <E> List<E> arrayList(E... elements) {
		List<E> list = new ArrayList<E>(elements.length);

		for (E e : elements)
			list.add(e);

		return list;
	}

	/**
	 * Create a new {@link HashMap}
	 */
	public static <K, V> Map<K, V> hashMap(K k1, V v1) {
		Map<K, V> map = new HashMap<>();

		map.put(k1, v1);

		return map;
	}

	/**
	 * Create a new {@link HashMap}
	 */
	public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2) {
		Map<K, V> map = new HashMap<>();

		map.put(k1, v1);
		map.put(k2, v2);

		return map;
	}

	/**
	 * Create a new {@link HashMap}
	 */
	public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> map = new HashMap<>();

		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);

		return map;
	}
}
