package com.ajaxjs.js;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class ListMap {
	public static void traveler(Map<String, Object> map, ListMapConfig config) {
		traveler(map, new Context(), null, 0, config);
	}

	@SuppressWarnings("unchecked")
	public static void traveler(Map<String, Object> map, Context fristCtx, Map<String, Object> superMap, int level,
			ListMapConfig config) {
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
	 * @param mapFn 	Map 对象的回调函数
	 * @param entryFn 	Map 对象身上各个 key/value 的回调函数
	 */
	public static void traveler(List<Map<String, Object>> list, ListMapConfig config) {
		traveler(list, new Context(), null, 0, config);
	}

	/**
	 * 遍历一个 ListMap
	 * 
	 * @param list 		输入的 ListMap
	 * @param mapFn 	Map 对象的回调函数
	 * @param fristCtx 	用于是否阻止遍历的变量，因为 java 非对象参数是按值的，不能修改那个对象，所以得用一个对象。
	 * @param superCtx 	父级上下文
	 */
	public static void traveler(List<Map<String, Object>> list, Context fristCtx, Map<String, Object> superMap,
			int level, ListMapConfig config) {
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

		String[] arr = str.split("/");
		Queue<String> q = new LinkedList<>(Arrays.asList(arr));

		return findByPath(q, list);
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

	// /**
	// * 在 Map List 中查找符合 key 的 map
	// *
	// * @param list map 列表
	// * @param str 目标 key
	// * @return Map
	// */
	// private static Map<String, Object> findMap(List<Map<String, Object>> list,
	// String str) {
	// for (Map<String, Object> map : list) {
	// if (map.get(id).toString().equals(str))
	// return map;
	// }
	//
	// return null;
	// }

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
