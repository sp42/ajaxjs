package com.ajaxjs.mvc.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

public abstract class TreeLinked<T> {
	/**
	 * 下级路径集合
	 */
	public Map<String, T> children;

	/**
	 * 
	 * @param treeNode
	 * @param queue
	 * @param path
	 * @param createIfEmpty 如果找不到该节点，是否自动为其创建节点？true 表示为写入模式，不单纯是查找。
	 * @return
	 */
	public static <T extends TreeLinked<T>> T findTreeByPath(Map<String, T> treeNode, Queue<String> queue, String path, Function<String, T> targetFactory, boolean createIfEmpty) {
		while (!queue.isEmpty()) {
			String key = queue.poll(); // remove the first item in the queue and return it
			path += key + "/";

			T target = null;
			if (treeNode.containsKey(key)) {
				target = treeNode.get(key);
			} else if (createIfEmpty) { // new value to be set
				target = targetFactory.apply(path);
				treeNode.put(key, target);
			}

			if (queue.isEmpty()) {
				return target;// found it!
			} else { // remains sub path to find out
				if (createIfEmpty && target.children == null) // set action
					target.children = new HashMap<>();

				T t2 = findTreeByPath(target.children, queue, path, targetFactory, createIfEmpty);
				if (t2 != null)
					return t2;
			}
		}

		return null;
	}

	public static <T extends TreeLinked<T>> T findTreeByPath(Map<String, T> treeNode, Queue<String> queue, String path) {
		return findTreeByPath(treeNode, queue, path, null, false);
	}
	
	public static Queue<String> split2Queue(String path) {
		String[] arr = path.split("/");
		return new LinkedList<>(Arrays.asList(arr));
	}
}