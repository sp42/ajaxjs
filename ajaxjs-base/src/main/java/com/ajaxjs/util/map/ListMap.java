/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.util.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.ajaxjs.util.map.ListMapConfig.Context;

/**
 * 遍历 ListMap 或 MapList
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ListMap {
	/**
	 * 遍历一个 MapList
	 * 
	 * @param map		输入的 MapList
	 * @param config	关于回调函数的配置
	 */
	public static void traveler(Map<String, Object> map, ListMapConfig config) {
		traveler(map, new Context(), null, 0, config);
	}

	/**
	 * 遍历一个 MapList
	 * 
	 * @param map		输入的 MapList
	 * @param fristCtx	用于是否退出遍历的变量
	 * @param superMap	父级 Map
	 * @param level		深度
	 * @param config	关于回调函数的配置
	 */
	@SuppressWarnings("unchecked")
	public static void traveler(Map<String, Object> map, Context fristCtx, Map<String, Object> superMap, int level, ListMapConfig config) {
		if (config != null && config.mapHandler != null && !config.mapHandler.execute(map, superMap, level))
			return;

		for (String key : map.keySet()) {
			if (fristCtx.isStop()) // 回调控制返回
				return;

			Object value = map.get(key);

			if (config != null && config.mapEntryHandler != null
					&& !config.mapEntryHandler.execute(key, value, map, superMap, level)) {
				fristCtx.setStop(true);
				return;
			}

			if (value != null && (value instanceof List || value instanceof Map)) {
				if (config != null && config.newKey != null)
					config.newKey.accept(key);

				if (value instanceof Map)
					traveler((Map<String, Object>) value, fristCtx, map, level + 1, config);
				if (value instanceof List)
					traveler((List<Map<String, Object>>) value, fristCtx, map, level + 1, config);

				if (config != null && config.exitKey != null)
					config.exitKey.accept(key);
			}
		}
	}

	/**
	 * 遍历一个 ListMap
	 * 
	 * @param list 		输入的 ListMap
	 * @param config 	关于回调函数的配置
	 */
	public static void traveler(List<Map<String, Object>> list, ListMapConfig config) {
		traveler(list, new Context(), null, 0, config);
	}

	/**
	 * 遍历一个 ListMap
	 * 
	 * @param list		输入的 ListMap
	 * @param fristCtx	用于是否退出遍历的变量
	 * @param superMap	父级 Map
	 * @param level		深度
	 * @param config	关于回调函数的配置
	 */
	public static void traveler(List<Map<String, Object>> list, Context fristCtx, Map<String, Object> superMap, int level, ListMapConfig config) {
		for (Map<String, Object> map : list) {
			if (map == null)
				continue;

			traveler(map, fristCtx, superMap, level, config);
		}
	}

	/**
	 * 为每个 Map 加上 path 和 level 字段，分别是路径和深度。默认字段为 "id", "fullPath", "level"。
	 * 
	 * @param list 输入的 ListMap
	 */
	public static void buildPath(List<Map<String, Object>> list) {
		buildPath(list, ID, PATH, LEVEL);
	}

	/**
	 * 为每个 Map 加上 path 和 level 字段，分别是路径和深度
	 * 
	 * @param list 	输入的 ListMap
	 * @param id 	map 的表示字段
	 * @param path 	map 的路径字段
	 * @param level map 的深度
	 */
	public static void buildPath(List<Map<String, Object>> list, String id, String path, String level) {
		ListMapConfig config = new ListMapConfig();
		config.mapHandler = (map, superMap, _level) -> {
			String superPath = superMap == null ? "" : superMap.get(path).toString();
			map.put(path, superPath + "/" + map.get(id));
			map.put(level, _level);

			// System.out.println(map.get(id) + "@" + _level + ":" + map.get(path));
			return true;
		};

		traveler(list, config);
	}

	/////////////////////////// findByPath /////////////////
	/**
	 * 输入一个路径，转换为队列结构
	 * 
	 * @param path 路径，用 / 分隔开
	 * @return 路径队列
	 */
	public static Queue<String> split2Queue(String path) {
		String[] arr = path.split("/");
		return new LinkedList<>(Arrays.asList(arr));
	}

	/**
	 * 根据路径查找节点
	 * 
	 * @param str  Key 列表字符
	 * @param list 列表
	 * @return Map
	 */
	public static Map<String, Object> findByPath(String str, List<Map<String, Object>> list) {
		if (str.startsWith("/"))
			str = str.substring(1, str.length());
		if (str.endsWith("/"))
			str = str.substring(0, str.length() - 1);

		return findByPath(split2Queue(str), list);
	}

	/**
	 * TODO
	 * @param str
	 * @param list
	 * @return
	 */
	public static Map<String, Object> findByPath(String str, Map<String, Object> list) {
		return null;
	}

	/**
	 * 
	 */
	public final static String ID = "id";
	public final static String CHILDREN = "children";
	public final static String PATH = "fullPath";
	public final static String LEVEL = "level";

	/**
	 * 真正的查找函数
	 * 
	 * @param queue 队列
	 * @param list  列表
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> findByPath(Queue<String> queue, List<Map<String, Object>> list) {
		Map<String, Object> map = null;

		while (!queue.isEmpty()) {
			String str = queue.poll();

			for (Map<String, Object> _map : list) {// 在 Map List 中查找符合 key 的 map
				if (_map.get(ID).toString().equals(str)) {
					map = _map;
					break;
				}
			}

			if (map != null) {
				if (queue.isEmpty()) {
					break;// found it!
				} else if (map.get(CHILDREN) != null) {
					map = findByPath(queue, (List<Map<String, Object>>) map.get(CHILDREN));
				} else {
					map = null;
				}
			}
		}

		return map;
	}

	/////////////////////////// flatMap ///////////////////////
	/**
	 * 扁平化多层 map 为单层
	 * 
	 * @param map 多层 Map
	 * @return 单层 Map
	 */
	public static Map<String, Object> flatMap(Map<String, Object> map) {
		final Stack<String> stack = new Stack<>();
		final Map<String, Object> _map = new HashMap<>();

		ListMapConfig config = new ListMapConfig();
		config.newKey = key -> stack.add(key); // 进栈
		config.exitKey = key -> stack.pop(); // 退栈
		config.mapEntryHandler = (key, obj, currentMap, superMap, i) -> {
			if (obj == null || obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
				StringBuilder sb = new StringBuilder();
				for (String s : stack) {
					sb.append(s + ".");
				}
				_map.put(sb + key, obj);
			}

			return true;
		};

		traveler(map, config);
		return _map;
	}
}
