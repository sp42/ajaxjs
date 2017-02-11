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

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

/**
 * json 转为 java 对象的工具类，利用了 JVM 自带的 JS 引擎
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
	public static ScriptEngine engineFactory() {
		return new ScriptEngineManager().getEngineByName(System.getProperty("java.version").contains("1.8.") ? "nashorn" : "rhino");
	}

	/**
	 * JVM 自带的 JS 引擎
	 */
	final static ScriptEngine engine = engineFactory();

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
	 * 转换为 map 或 list，可指定 JS 引擎和 js 命名空间
	 * 
	 * @param js
	 *            JSON 字符串
	 * @param key
	 *            js 命名空间 JSON Path，可以带有 aa.bb.cc
	 * @param clazz
	 *            目标类型
	 * @return 目标对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T accessMember(ScriptEngine engine, String js, String key, Class<T> clazz) {
		T result = null;

		String jsCode = null;
//		System.out.println("var obj = " + js);
		try {
			jsCode = "var obj = " + js;
			/*
			 * rhino 不能直接返回 map，如 eval("{a:1}")-->null，必须加变量，例如 执行 var xx = {...};
			 */
			engine.eval(jsCode);
			
			Object obj;
			if (key == null) {// 直接返回变量
				jsCode = "obj;";
				obj = engine.eval(jsCode);
			} else {
				if (key.contains(".")) {
					jsCode = "obj." + key + ";";
					obj = engine.eval(jsCode);
				} else {
					jsCode = "obj['" + key + "'];";
					obj = engine.eval(jsCode);
				}
			}
			result = (T) obj;// 转换目标类型
			
		} catch (ScriptException e) {
			System.err.println("脚本eval()运算发生异常！eval 代码：" + jsCode);
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 转换为 map 或 list，可指定 JS 引擎
	 * @param engine
	 * @param js
	 * @param clazz
	 * @return
	 */
	public static <T> T accessMember(ScriptEngine engine, String js, Class<T> clazz) {
		return accessMember(engine, js, null, clazz);
	}
	
	/**
	 * 转换为 map 或 list，默认的 JS 引擎
	 * @param js
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T accessMember(String js, String key, Class<T> clazz) {
		return accessMember(engine, js, key, clazz);
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
	
	/**
	 * 执行 js 任意代码
	 * @param engine
	 * @param code
	 * @param clazz
	 * @return
	 */
	public static <T> T eval(ScriptEngine engine, String code, Class<T> clazz) {
		if (StringUtil.isEmptyString(code))
			throw new UnsupportedOperationException("JS 代码不能为空！");

		Object obj = null;

		try {
			obj = engine.eval(code);
		} catch (ScriptException e) {
			System.err.println("脚本eval()运算发生异常！eval 代码：" + code);
			e.printStackTrace();
		}

		if (obj != null && clazz != null) { // 当 clazz ＝ null 表示只是执行，不要求返回结果
			// return Util.TypeConvert(js.eval(code), clazz); // 为什么要执行多次？
			T _obj = Util.TypeConvert(obj, clazz);
			return _obj;
		} else
			return null;
	}
	
	/**
	 * 执行 js 任意代码
	 * @param code
	 * @param clazz
	 * @return
	 */
	public static <T> T eval(String code, Class<T> clazz) {
		return eval(engine, code, clazz);
	}
	
	/**
	 * 执行 js 任意代码

	 * @param clazz
	 * @return
	 */
	public static <T> T eval(String code) {
		return eval(engine, code, null);
	}
}
