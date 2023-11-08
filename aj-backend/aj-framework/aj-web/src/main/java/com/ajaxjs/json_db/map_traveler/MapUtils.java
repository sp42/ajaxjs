package com.ajaxjs.json_db.map_traveler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class MapUtils {
	/**
	 * 扁平化多层 map 为单层
	 *
	 * @param map 多层 Map
	 * @return 单层 Map
	 */
	public static Map<String, Object> flatMap(Map<String, Object> map) {
		final Stack<String> stack = new Stack<>();
		final Map<String, Object> _map = new HashMap<>();

		MapTraveler traveler = new MapTraveler();

		traveler.setOnNewKey(stack::add);// 进栈
		traveler.setOnExitKey(key -> stack.pop());// 退栈
		traveler.setOnNode((key, obj, currentMap, superMap, i) -> {
			if (obj == null || obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
				StringBuilder sb = new StringBuilder();

				for (String s : stack)
					sb.append(s).append("_");

				_map.put(sb + key, obj);
			}

			return true;
		});

		traveler.traveler(map);

		return _map;
	}

	public final static String ID = "id";

	public final static String CHILDREN = "children";

	public final static String PATH = "fullPath";

	public final static String LEVEL = "level";

	/**
	 * 为每个 Map 加上 path 和 level 字段，分别是路径和深度。默认字段为 "id", "fullPath", "level"。
	 *
	 * @param list       输入的 ListMap
	 * @param saveSupers 是否生成父级节点的信息（包含 id 和名称）
	 */
	public static void buildPath(List<Map<String, Object>> list, boolean saveSupers) {
		buildPath(list, ID, PATH, LEVEL, saveSupers);
	}

	public static void buildPath(List<Map<String, Object>> list) {
		buildPath(list, ID, PATH, LEVEL, false);
	}

	public static void buildPath(List<Map<String, Object>> list, String id, String path, String level) {
		buildPath(list, id, path, level, false);
	}

	/**
	 * 为每个 Map 加上 path 和 level 字段，分别是路径和深度
	 *
	 * @param list  输入的 ListMap
	 * @param id    map 的表示字段
	 * @param path  map 的路径字段
	 * @param level map 的深度
	 */
	public static void buildPath(List<Map<String, Object>> list, String id, String path, String level,
			boolean saveSupers) {
		MapTraveler traveler = new MapTraveler();

		traveler.setOnMap((map, superMap, _level) -> {
			String superPath = superMap == null ? "" : superMap.get(path).toString();
			map.put(path, superPath + "/" + map.get(id));
			map.put(level, _level);

			if (saveSupers && superMap != null) {
				Object _supers = superMap.get("supers");
				String supers = _supers == null ? "" : _supers.toString();

				supers += ("".equals(supers) ? "" : ",");
				supers += superPath + ":" + superMap.get("name");
				map.put("supers", supers);
				// supers.add(superPath + ":" + superMap.get("name"));
			}

			// LOGGER.info(map.get(id) + "@" + _level + ":" + map.get(path));
			return true;
		});
		traveler.traveler(list);
	}

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
				if (queue.isEmpty())
					break;// found it!
				else if (map.get(CHILDREN) != null)
					map = findByPath(queue, (List<Map<String, Object>>) map.get(CHILDREN));
				else
					map = null;
			}
		}

		return map;
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
			str = str.substring(1);
		
		if (str.endsWith("/"))
			str = str.substring(0, str.length() - 1);

		return findByPath(split2Queue(str), list);
	}
}
