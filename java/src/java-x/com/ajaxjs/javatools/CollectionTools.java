package com.ajaxjs.javatools;

public class CollectionTools {
	/**
	 * 移除数组中的第一个元素 remove the first element
	 * 
	 * @param arr
	 *            输入的数组
	 * @return
	 */
	public static Object[] shift(Object[] arr) {
		System.arraycopy(arr, 1, arr, 0, arr.length - 1);
		return arr;
	}
}
