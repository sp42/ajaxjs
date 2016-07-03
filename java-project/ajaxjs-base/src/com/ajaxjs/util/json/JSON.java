/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.util.json;

import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * json 转为 java 对象的工具类
 * 
 * @author frank
 *
 */
public class JSON {
	/**
	 * 创建 js 引擎工厂，支持 java 6/7 的 rhino 和 java 8 的 nashorn
	 * 
	 * @return js 引擎
	 */
	public static ScriptEngine engineFatory() {
		return new ScriptEngineManager()
				.getEngineByName(System.getProperty("java.version").contains("1.8.") ? "nashorn" : "rhino");
	}

	/**
	 * JVM 自带的 JS 引擎
	 */
	private final static ScriptEngine engine = engineFatory();

	/**
	 * 读取 json 里面的 map
	 * 
	 * @param js
	 *            JSON 字符串
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return Map 对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(String js, String key) {
		return (Map<String, Object>) accessMember(js, key, Map.class);
	}

	/**
	 * 读取 json 里面的 map
	 * 
	 * @param js
	 *            JSON 字符串
	 * @return Map 对象
	 */
	public static Map<String, Object> getMap(String js) {
		return getMap(js, null);
	}

	/**
	 * 转换为 map 或 list
	 * 
	 * @param js
	 *            JSON 字符串
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @param clazz
	 *            目标类型
	 * @return 目标对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T accessMember(String js, String key, Class<T> clazz) {
		T result = null;

		try {
			engine.eval("var obj = " + js);// rhino 不能直接返回 map，如 eval("{a:1}")
											// -->null，必须加变量，例如 执行 var xx =
											// {...};
			Object obj;
			if (key == null) {
				obj = engine.eval("obj;");
			} else {
				if (key.contains(".")) {
					obj = engine.eval("obj." + key + ";");
				} else {
					obj = engine.eval("obj['" + key + "'];");
				}
			}
			result = (T) obj;
		} catch (ScriptException e) {
			System.err.println("脚本eval()运算发生异常！eval 代码：" + js);
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 读取 json 里面的 list，list 里面每一个都是 map
	 * 
	 * @param js
	 *            JSON 字符串
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return 包含 Map 的列表
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getList(String js, String key) {
		return (List<Map<String, Object>>) accessMember(js, key, List.class);
	}

	/**
	 * 读取 json 里面的 list，list 里面每一个都是 map
	 * 
	 * @param js
	 *            JSON 字符串
	 * @return 包含 Map 的列表
	 */
	public static List<Map<String, Object>> getList(String js) {
		return getList(js, null);
	}

	/**
	 * 读取 json 里面的 list，list 里面每一个都是 String
	 * 
	 * @param js
	 *            JSON 字符串
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return 包含 String 的列表
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getStringList(String js, String key) {
		return (List<String>) accessMember(js, key, List.class);
	}

	/**
	 * 读取 json 里面的 list，list 里面每一个都是 String
	 * 
	 * @param js
	 *            JSON 字符串
	 * @return 包含 String 的列表
	 */
	public static List<String> getStringList(String js) {
		return getStringList(js, null);
	}

	/**
	 * js number 为 double 类型，在 java 里面使用不方便，将其转换为 int
	 * 
	 * @param d
	 *            js number
	 * @return int 值
	 */
	public static int double2int(Double d) {
		if (d > Integer.MAX_VALUE) {
			System.out.println(d + "数值太大，不应用这个方法转换到 int");
			return 0;
		} else {
			return d.intValue();
		}

	}
}
