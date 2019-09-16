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
package com.ajaxjs.mvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * 控制器 一个 Controller 通常拥有多个方法，每个方法负责处理一个 URL。 直接把一个 URL 映射到一个方法
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public interface IController {
	/**
	 * URL 与 Action 之间的映射树。URL Mapping tree.
	 */
	public static Map<String, Action> urlMappingTree = new HashMap<>();

	/**
	 * 
	 * @param tree		保存在Map的一棵树
	 * @param queue			队列
	 * @param path
	 * @param createIfEmpty 如果找不到该节点，是否自动为其创建节点？若为非null 表示为写入模式，不单纯是查找。
	 * @return
	 */
	public static Action findTreeByPath(Map<String, Action> tree, Queue<String> queue, String path, boolean createIfEmpty) {
		while (!queue.isEmpty()) {
			String key = queue.poll(); // remove the first item in the queue and return it
			path += key + "/";
	
			Action target = null;
			if (tree.containsKey(key)) {	// 找到
				target = tree.get(key);
			} else if (createIfEmpty) { // 新建Action
				target = new Action();
				target.path = path.replaceAll(".$", "");
				
				tree.put(key, target);
			}
	
			if (queue.isEmpty()) {
				return target;// found it!
			} else { // remains sub path to find out
				if (createIfEmpty && target.children == null) // set action
					target.children = new HashMap<>();
	
				if (target != null) {
					Action t2 = findTreeByPath(target.children, queue, path, createIfEmpty);
					if (t2 != null)
						return t2;
				}
			}
		}
	
		return null;
	}

	/**
	 * 
	 * @param tree
	 * @param queue
	 * @param path
	 * @return
	 */
	public static Action findTreeByPath(Map<String, Action> tree, Queue<String> queue, String path) {
		return findTreeByPath(tree, queue, path, false);
	}
	
}