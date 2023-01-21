package com.batch_read;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用于Map中根据key值排序用
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class MapUtil {
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (String.valueOf(o1.getKey())).compareTo(String.valueOf(o2.getKey()));
			}
		});

		Map<K, V> result = new LinkedHashMap<>();

		for (Map.Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());

		return result;
	}
}