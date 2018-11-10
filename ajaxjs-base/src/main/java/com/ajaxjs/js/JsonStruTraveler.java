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
package com.ajaxjs.js;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * JSON 结构的遍历者 注意输入必须为 list 而不能是 map
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class JsonStruTraveler {
	/**
	 * 
	 */
	private String id = "id";

	/**
	 * 
	 */
	private String children = "children";

	/**
	 * 根据路径查找节点
	 * 
	 * @param str  Key 列表字符
	 * @param list 列表
	 * @return Map
	 */
	public Map<String, Object> findByPath(String str, List<Map<String, Object>> list) {
		if (str.startsWith("/"))
			str = str.substring(1, str.length());
		if (str.endsWith("/"))
			str = str.substring(0, str.length() - 1);

		String[] arr = str.split("/");
		Queue<String> q = new LinkedList<>(Arrays.asList(arr));

		return findByPath(q, list);
	}

	/**
	 * 真正的查找函数
	 * 
	 * @param queue 队列
	 * @param list  列表
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> findByPath(Queue<String> queue, List<Map<String, Object>> list) {
		Map<String, Object> map = null;

		while (!queue.isEmpty()) {
			map = findMap(list, queue.poll());

			if (map != null) {
				if (queue.isEmpty()) {
					break;// found it!
				} else if (map.get(children) != null) {
					map = findByPath(queue, (List<Map<String, Object>>) map.get(children));
				} else {
					map = null;
				}
			}
		}

		return map;
	}

	/**
	 * 在 Map List 中查找符合 key 的 map
	 * 
	 * @param list map 列表
	 * @param str  目标 key
	 * @return Map
	 */
	private Map<String, Object> findMap(List<Map<String, Object>> list, String str) {
		for (Map<String, Object> map : list) {
			if (map.get(id).toString().equals(str))
				return map;
		}

		return null;
	}

	/**
	 * 分析这棵树，为每个节点增加 fullPath 和 level 属性，分别表示完整的路径和层数
	 * 
	 * @param list 输入的树，必须为 List
	 */
	public void travelList(List<Map<String, Object>> list) {
		travelList(list, null, 0);
	}

	/**
	 * 内部递归用的函数
	 * 
	 * @param list      输入的树，必须为 List
	 * @param superNode 父级节点，一开始可以为 null
	 * @param level     层数，一开始可以为 0
	 */
	@SuppressWarnings("unchecked")
	private void travelList(List<Map<String, Object>> list, Map<String, Object> superNode, int level) {
		for (Map<String, Object> map : list) {
			if (map != null) {
				String currerntPath = (superNode != null ? superNode.get("fullPath").toString() : "") + "/"
						+ map.get(id).toString();
				map.put("fullPath", currerntPath);
				map.put("level", level);

				// 记录父级信息
				List<String> supers = new ArrayList<>();
				map.put("supers", supers);

				if (superNode != null) {
					supers.addAll((List<String>) superNode.get("supers"));
					supers.add(superNode.get("fullPath") + ":" + superNode.get("name")); // 仅记录 id 和 name
				}

				if (map.get(children) != null && map.get(children) instanceof List)
					travelList((List<Map<String, Object>>) map.get(children), map, level + 1);
			}
		}
	}

	/**
	 * 遍历 MapList，允许 TravelMapList_Iterator 控制
	 * 
	 * @param map      输入 Map
	 * @param iterator 回调
	 */
	@SuppressWarnings("unchecked")
	public static void travelMapList(Map<String, Object> map, TravelMapList_Iterator iterator) {
		for (String key : map.keySet()) {
			Object obj = map.get(key);

			if (iterator != null)
				iterator.handler(key, obj);

			if (obj != null) {
				if (obj instanceof Map) {
					if (iterator != null)
						iterator.newKey(key);

					travelMapList((Map<String, Object>) obj, iterator);

					if (iterator != null)
						iterator.exitKey(key);
				} else if (obj instanceof List) {
					List<Object> list = (List<Object>) obj;

					for (Object item : list) {
						if (item != null && item instanceof Map)
							travelMapList((Map<String, Object>) item, iterator);
					}
				}
			}
		}
	}

	/**
	 * 遍历 MapList 的回调
	 * 
	 * @author Sp42 frank@ajaxjs.com
	 */
	public static interface TravelMapList_Iterator {
		/**
		 * 当得到了 Map 的 key 和 value 时调用
		 * 
		 * @param key 键名称
		 * @param obj 键值
		 */
		public void handler(String key, Object obj);

		/**
		 * 当得到一个新的 key 时候
		 * 
		 * @param key 键名称
		 */
		public void newKey(String key);

		/**
		 * 当退出一个当前 key 的时候
		 * 
		 * @param key 键名称
		 */
		public void exitKey(String key);

	}

	/**
	 * 扁平化多层 map 为单层
	 * 
	 * @param map 多层 Map
	 * @return 单层 Map
	 */
	public static Map<String, Object> flatMap(Map<String, Object> map) {
		final Stack<String> stack = new Stack<>();
		final Map<String, Object> _map = new HashMap<>();

		travelMapList(map, new TravelMapList_Iterator() {
			@Override
			public void handler(String key, Object obj) {
				if (obj == null || obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
					StringBuilder sb = new StringBuilder();
					for (String s : stack) {
						sb.append(s + ".");
					}
					_map.put(sb + key, obj);
				}
			}

			@Override
			public void newKey(String key) {
				stack.add(key); // 进栈
			}

			@Override
			public void exitKey(String key) {
				stack.pop(); // 退栈
			}
		});

		return _map;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the children
	 */
	public String getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(String children) {
		this.children = children;
	}
}
