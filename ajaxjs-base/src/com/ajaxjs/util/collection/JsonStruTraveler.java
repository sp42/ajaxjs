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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * JSON 结构的遍历者 注意输入必须为 list 而不能是 map
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class JsonStruTraveler {
	private String id = "id";
	private String children = "children";

	/**
	 * 
	 * @param str
	 *            Key 列表字符
	 * @param list
	 *            列表
	 * @return Map
	 */
	public Map<String, Object> find(String str, List<Map<String, Object>> list) {
		if (str.startsWith("/"))
			str = str.substring(1, str.length());
		if (str.endsWith("/"))
			str = str.substring(0, str.length() - 1);

		return find(str.split("/"), list);
	}

	/**
	 * 
	 * @param arr
	 *            Key 列表
	 * @param list
	 *            列表
	 * @return Map
	 */
	public Map<String, Object> find(String[] arr, List<Map<String, Object>> list) {
		Stack<String> stack = new Stack<>();

		for (int i = arr.length - 1; i >= 0; i--)
			stack.push(arr[i]);

		return find(stack, list);
	}

	/**
	 * 真正的查找函数
	 * 
	 * @param stack
	 *            栈
	 * @param list
	 *            列表
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> find(Stack<String> stack, List<Map<String, Object>> list) {
		Map<String, Object> map = null;

		while (!stack.empty()) {
			map = findMap(list, stack.pop());

			if (map != null) {
				if (stack.empty()) {
					break;// found it!
				} else if (map.get(children) != null) {
					map = find(stack, (List<Map<String, Object>>) map.get(children));
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
	 * @param list
	 *            map 列表
	 * @param str
	 *            目标 key
	 * @return Map
	 */
	private Map<String, Object> findMap(List<Map<String, Object>> list, String str) {
		for (Map<String, Object> map : list) {
			if (map.get(id).toString().equals(str))
				return map;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public void travle(Map<String, Object> map) {
		for (String key : map.keySet()) {
			Object obj = map.get(key);

			if (obj != null && obj instanceof Map) {
				Map<String, Object> _map = (Map<String, Object>) obj;
				System.out.println(_map.get("name"));
				if (_map.get(children) != null && _map.get(children) instanceof List) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) _map.get(children);

					for (Map<String, Object> __map : list)
						travle(__map);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void travleList(List<Map<String, Object>> list, Map<String, Object> superNode, int level) {
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
					travleList((List<Map<String, Object>>) map.get(children), map, level + 1);
			}
		}
	}

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

	public static Map<String, Object> flatMap(Map<String, Object> map) {
		final Stack<String> stack = new Stack<>();
		final Map<String, Object> _map = new HashMap<>();
		
		travelMapList(map, new TravelMapList_Iterator() {
			@Override
			public void handler(String key, Object obj) {
				if(obj == null || obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
					StringBuilder sb = new StringBuilder();
					for (String s : stack) {
						sb.append(s + ".");
					}
					_map.put(sb + key, obj);
//					System.out.println(sb + key + ":" + obj);
				}
			}

			@Override
			public void newKey(String key) {
				stack.add(key);
			}

			@Override
			public void exitKey(String key) {
				stack.pop();
			}
		});
		
		return _map;
	}

	public static interface TravelMapList_Iterator {
		/**
		 * 当得到了 Map 的 key 和 value 时调用
		 * 
		 * @param key
		 * @param obj
		 */
		public void handler(String key, Object obj);

		public void exitKey(String key);

		public void newKey(String key);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param children
	 *            the children to set
	 */
	public void setChildren(String children) {
		this.children = children;
	}
}
