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
package com.ajaxjs.js.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import com.ajaxjs.util.Value;

/**
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class JSON extends JsEngineWrapper {
	public JSON() {
		super();
	}
	
	public JSON(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public JSON(ScriptEngine jsengine) {
		super(jsengine);
	}
	
	/**
	 * json 字符串，通常从接口返回的
	 */
	private String jsonString;
	
	/**
	 * 是否在 list 将里面的 map 所包含的 double 都转为 int？
	 * 如果设为 false 则性能好很多，但就不方便了，只适合没有数字类型的 json 字段
	 * 另外，如果要转换类似 [{a:'hello'}, 123, true] 这样不是 map 的 list，也要将本项设为 false
	 */
	private boolean isAutoDouble2IntInList = true;
	
	/**
	 * 
	 */
	private boolean deep;
	
	/**
	 * rhino 不能直接返回 map，如 eval("{a:1}")-->null，必须加变量，例如 执行 var xx = {...};
	 * 
	 * @param key
	 *            js 命名空间 JSON Path，可以带有 . 分隔符的，如 aa.bb.cc
	 * @param clazz
	 *            目标类型
	 * @return 目标对象
	 */
	public <T> T accessJsonMember(String key, Class<T> clazz) {
		T result = null;

		String jsCode = "var obj = " + getJsonString();

		eval(jsCode);
			
		if (key == null) {	// 直接返回变量
			jsCode = "obj;";
			result = eval(jsCode, clazz);
		} else {
//			if (key.contains(".")) {
				jsCode = "obj." + key + ";";
				result = eval(jsCode, clazz);
//			} else {
//				jsCode = "obj['" + key + "'];";// TODO 这是多余的？
//				result = eval(jsCode, clazz);
//			}
		} 
			
		return result;
	}

	/**
	 * 读取 json 里面的 map
	 * 
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return Map 对象
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String key) {
		Map<String, Object> map = (Map<String, Object>)accessJsonMember(key, Map.class);
	
		return deep ? deep(map) : double2int4Map(map);
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> deep(Map<String, Object> map) {
		// 转换为真正的 map
		Map<String, Object> realMap = new HashMap<>();

		for (String _key : map.keySet()) {
			// js double -->int
			Object obj = map.get(_key);

			if (obj instanceof Double) {
				realMap.put(_key, Value.double2int((Double) map.get(_key)));
			} else if (obj instanceof List) {
				List<?> list = (List<?>) obj;
				List<Object> _list = new ArrayList<>();

				if (list.get(0) != null) {
					if (list.get(0) instanceof Map) {
						for (Map<String, Object> _map : (List<Map<String, Object>>) obj)
							_list.add(deep(_map));
					} else if (list.get(0) instanceof Double) { // 数字
						for (Double d : (List<Double>) obj)
							_list.add(Value.double2int(d));
					}
				} else {
					//
				}

				realMap.put(_key, _list);
			} else {
				realMap.put(_key, map.get(_key));
			}
		}

		return realMap;
	}

	/**
	 * 读取 json 里面的 list，list 里面每一个都是 map
	 * 
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return 包含 Map 的列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getList(String key) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) accessJsonMember(key, List.class);
		
		if(isAutoDouble2IntInList()) 
			list = double2int(list);
		
		return list; 
	} 
	
	/**
	 * 读取 json 里面的 list，list 里面每一个都是 String
	 * 
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return 包含 String 的列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key) {
		return (List<String>) accessJsonMember(key, List.class);
	} 

	/**
	 * js 的数字类型是 double，如果在 List<Map> 里面的话需要遍历之，取出其中的 double 变为 int
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> double2int(List<Map<String, Object>> list) {
		List<Map<String, Object>> _list = new ArrayList<>();

		for (Map<String, Object> map : list) 
			_list.add(double2int4Map(map));

		return _list;
	}

	/**
	 * js 的数字类型是 double，如果在 map 里面的话需要遍历之，取出其中的 double 变为 int
	 * @param map
	 * @return
	 */
	private static Map<String, Object> double2int4Map(Map<String, Object> map) {
		// 转换为真正的 map
		Map<String, Object> realMap = new HashMap<>();
		
		for (String _key : map.keySet()) {
			// js double -->int
			Object obj = map.get(_key);

			if (obj instanceof Double) {
				realMap.put(_key, Value.double2int((Double) map.get(_key)));
			} else {
				realMap.put(_key, map.get(_key));
			}
		}

		return realMap;
	}
	

	/**
	 * 借助 js 序列化对象为 json 明明有 jsonString 为何还要 stringify？
	 * 
	 * @return JSON 字符串
	 */
	@Deprecated
	public String stringify() {
		return eval("JSON.stringify(" + getJsonString() + ");", String.class);
	}

	/**
	 * 借助 js 序列化对象为 json
	 * 
	 * @param key
	 *            js 表达式
	 * @return JSON 字符串
	 */
	public String stringify(String key) {
		return eval("JSON.stringify(" + key + ");", String.class);
	}

	/**
	 * 借助 js 序列化对象为 json stringify() 不能传对象，故使用这方法
	 * 
	 * @param obj
	 *            NativeArray | NativeObject 均可
	 * @return JSON 字符串
	 */
	public String stringifyObj(Object obj) {
		return call("stringify", String.class, eval("JSON"), obj);
	}

	public String getJsonString() {
		return jsonString;
	}

	public JSON setJsonString(String jsonString) {
		this.jsonString = jsonString;
		return this;
	}

	public boolean isAutoDouble2IntInList() {
		return isAutoDouble2IntInList;
	}

	public void setAutoDouble2IntInList(boolean isAutoDouble2IntInList) {
		this.isAutoDouble2IntInList = isAutoDouble2IntInList;
	}

	public boolean isDeep() {
		return deep;
	}

	public JSON setDeep(boolean deep) {
		this.deep = deep;
		
		return this;
	}  
}
