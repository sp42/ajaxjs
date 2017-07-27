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
package com.ajaxjs.js;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * JSON 解构的遍历者
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
	public void travleList(List<Map<String, Object>> list, String superPath, int level) {
		for (Map<String, Object> map :list) {
			if (map != null) {
				String currerntPath = superPath + "/" + map.get(id).toString();
				map.put("fullPath", currerntPath);
				map.put("level", level);
				
				if (map.get(children) != null && map.get(children) instanceof List) 
					travleList((List<Map<String, Object>>) map.get(children), currerntPath, level + 1);
			}
		}
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
