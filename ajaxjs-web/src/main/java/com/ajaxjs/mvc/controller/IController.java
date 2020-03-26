/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.mvc.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajaxjs.util.map.ListMap;

/**
 * 控制器 一个 Controller 通常拥有多个方法，每个方法负责处理一个 URL。 直接把一个 URL 映射到一个方法
 * 
 * @author sp42 frank@ajaxjs.com
 */
public interface IController {

	/**
	 * URL 与 Action 之间的映射树。URL Mapping tree.
	 */
	public static Map<String, Action> urlMappingTree = new HashMap<>();

	/**
	 * 输入队列，查找节点。可选创建新 Action 节点，如果不存在的话。
	 * 
	 * @param tree          保存在Map的一棵树
	 * @param path          路径队列
	 * @param basePath      起始路径，如为空应传空字符串
	 * @param createIfEmpty 如果找不到该节点，是否自动为其创建节点？若为非null 表示为写入模式，不单纯是查找。
	 * @return 目标 Action 或新建的 Action
	 */
	public static Action findTreeByPath(Map<String, Action> tree, Queue<String> path, String basePath,
			boolean createIfEmpty) {
		while (!path.isEmpty()) {

			String key = path.poll(); // remove the first item in the queue and return it
			basePath += key + "/";

			Action target = null;
			if (tree.containsKey(key)) { // 找到
				target = tree.get(key);
			} else if (createIfEmpty) { // 新建Action
				target = new Action();
				target.path = basePath.replaceAll(".$", "");

				tree.put(key, target);
			}

			if (path.isEmpty()) {
				return target;// found it!
			} else { // remains sub path to find out
				if (createIfEmpty && target.children == null) // set action
					target.children = new HashMap<>();

				if (target != null) {
					Action t2 = findTreeByPath(target.children, path, basePath, createIfEmpty);
					if (t2 != null)
						return t2;
				} else {
					break;
				}
			}
		}

		return null;
	}

	/**
	 * 
	 * @param tree
	 * @param queue
	 * @param basePath
	 * @return
	 */
	public static Action findTreeByPath(Map<String, Action> tree, Queue<String> queue, String basePath) {
		return findTreeByPath(tree, queue, basePath, false);
	}

	public static Action findTreeByPath(Map<String, Action> tree, String path, String basePath, boolean createIfEmpty) {
		return findTreeByPath(tree, ListMap.split2Queue(path), basePath, createIfEmpty);
	}

	public static Action findTreeByPath(Map<String, Action> tree, String path, String basePath) {
		return findTreeByPath(tree, ListMap.split2Queue(path), basePath);
	}

	static final Pattern idRegexp = Pattern.compile("/\\d+");

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static Action findTreeByPath(String path) {
		Matcher match = idRegexp.matcher(path); // 处理Path上的参数
		
		if (match.find()) {
			path = match.replaceAll("/{id}");
			System.out.println(">>>>>>>>>" + path);
		}

		Action action = findTreeByPath(urlMappingTree, path, "");

//		if (action == null) { // for the controller which is set Path
//			action = findTreeByPath(urlMappingTree, split2Queue2(path), "");
//		}

		return action;
	}

	/**
	 * Covernt "aa/bb/cc" to queen.
	 * 
	 * @param path
	 * @return
	 */
	static Queue<String> split2Queue2(String path) {
		String[] arr = path.split("/");

		if (arr.length == 1)
			arr = new String[] { "", arr[0] }; // for the case of the root

		return new LinkedList<>(Arrays.asList(arr));
	}

}