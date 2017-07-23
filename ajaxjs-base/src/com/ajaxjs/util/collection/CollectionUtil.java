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
package com.ajaxjs.util.collection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 * 
 * @author Frank Cheung frank@ajaxjs.com
 *
 */
public class CollectionUtil {
	/**
	 * 当它们每一个都是数字的字符串形式，转换为整形的数组 '["1", "2", ...]' --> [1, 2, ...]
	 * 
	 * @param value
	 *            输入字符串
	 * @param diver
	 *            分隔符
	 * @return 整形数组
	 */
	public static int[] strArr2intArr(String value, String diver) {
		String[] strArr = value.split(diver);
		int[] intArr = new int[strArr.length];

		for (int i = 0; i < strArr.length; i++)
			intArr[i] = Integer.parseInt(strArr[i].trim());

		return intArr;
	}

	/**
	 * 整形数组转换为字符数组 [1, 2, ...] --> '["1", "2", ...]'
	 * 
	 * @param arr
	 *            输入的整形数组
	 * @return 字符数组
	 */
	public static String[] int_arr2string_arr(int[] arr) {
		String[] strs = new String[arr.length];

		for (int i = 0; i < arr.length; i++)
			strs[i] = arr[i] + "";

		return strs;
	}

	/**
	 * List<String> 转换为字符串数组／数组效的话率更高一些
	 * 
	 * @param list
	 *            字符串列表
	 * @return 字符串数组
	 */
	public static String[] stringList2arr(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

	/**
	 * List<Integer> 转换为数组。数组的话效率更高一些
	 * 
	 * @param list
	 *            整形列表
	 * @return 整形数组
	 */
	public static Integer[] integerList2integer_arr(List<Integer> list) {
		return list.toArray(new Integer[list.size()]);
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static int[] integerList2arr(List<Integer> list) {
		int[] arr = new int[list.size()];

		for (int i = 0; i < list.size(); i++)
			arr[i] = list.get(i);

		return arr;
	}

	/**
	 * 判断数组是否有意义
	 * 
	 * @param arr
	 *            输入的数组
	 * @return true 表示为素组不是为空，是有内容的，false 表示为数组为空数组，length = 0
	 */
	public static boolean isNotNull(Object[] arr) {
		return arr != null && arr.length > 0;
	}

	/**
	 * 判断 collection 是否有意义
	 * 
	 * @param collection
	 *            Map输入的集合
	 * @return true 表示为集合不是为空，是有内容的，false 表示为空集合
	 */
	public static boolean isNotNull(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 判断 map 是否有意义
	 * 
	 * @param map
	 *            输入的
	 * @return true 表示为 map 不是为空，是有内容的，false 表示为空 map
	 */
	public static boolean isNotNull(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}

}
