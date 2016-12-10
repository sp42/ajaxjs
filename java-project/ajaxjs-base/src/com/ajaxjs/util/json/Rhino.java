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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

/**
 * Only for JDK 6/7,JDK8 里面用不了
 * @author sp42
 *
 */
public class Rhino extends JSON {

	/**
	 * JVM 自带的 JS 引擎
	 */
	private final static ScriptEngine engine = engineFactory();
	
	public Map<String, Object> eval_return_Map(String code, String varName) {
		eval(engine, code, null); 

		return NativeObject2Map(eval(engine, varName, NativeObject.class)); // 提取变量
	}

	/**
	 * 输入 Js Array 代码（字符串），返回 Java String List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 String[]
	 */
	public String[] eval_return_StringArray(String code) {
		NativeArray obj = eval(engine, code, NativeArray.class);
		return NativeArray2StringArray(obj);
	}

	/**
	 * 输入 Js Array 代码（字符串），返回 Java Map List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 Map<String, Object>[]
	 */
	public Map<String, Object>[] eval_return_MapArray(String code) {
		NativeArray arr = eval(engine, code, NativeArray.class);
		return NativeArray2MapArray(arr);
	}

	public Map<String, Object>[] eval_return_MapArray(NativeArray arr) {
		return NativeArray2MapArray(arr);
	}
		
	/**
	 * JS Object 对象转换到 Java Hash 对象
	 * 
	 * @param obj
	 *            JS 里面的 Map
	 * @return Java 里面的 Map
	 */
	public static Map<String, Object> NativeObject2Map(NativeObject obj) {
		if (obj == null)
			return null;

		Map<String, Object> map = new HashMap<>();
		for (Object id : obj.getAllIds()) {// 遍历对象
			String newId = id.toString();
			Object value = obj.get(newId, obj);
			map.put(newId, JsonHelper.jsValue2java(value));
		}

		return map;
	}
	
	/**
	 * JS Array 对象转换到 Java String Array 对象
	 * 
	 * @param arr
	 *            JS 里面的数组，为 String 组成
	 * @return Java 里面的 Map[]
	 */
	public static String[] NativeArray2StringArray(NativeArray arr) {
		if (arr == null || arr.size() < 1) return null;

		return (String[]) arr.toArray(new String[arr.size()]);
	}
	
	/**
	 * JS Array 对象转换到 Java 任意 Array 对象 NativeArray arr = eval(code,
	 * NativeArray.class); return Mapper.NativeArray2Map(arr, Map.class);
	 * 
	 * @param arr
	 *            JS 数组
	 * @param clazz
	 *            要转换的目标类型
	 * @return 已转换的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] NativeArrayAs(NativeArray arr, Class<T> clazz) {
		if (arr == null || arr.size() < 1) return null;

		return (T[]) arr.toArray((T[]) Array.newInstance(clazz, arr.size()));
	}

	/**
	 * JS Array 对象转换到 Java Map Array 对象
	 * 
	 * @param arr
	 *            JS 里面的数组，为 Map 组成
	 * @return Java 里面的 Map[]
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] NativeArray2MapArray(NativeArray arr) {
//		if (!com.ajaxjs.util.Util.isNotNull(arr))// NativeArray.isEmpty 有 bug，
//			return null;
		if (arr == null || arr.size() < 1) return null;
		 
		// Not real Map!
		// return (Map<String, Object>[])arr.toArray(new Map[arr.size()]);

		Map<String, Object>[] _arr = new HashMap[arr.size()]; // java 不支持泛型数组
		
		int i = 0;
		for (Object obj : arr) { // JDK7 supports directly
			if (obj instanceof NativeObject) {
				_arr[i++] = NativeObject2Map((NativeObject) obj);
			} else if (obj instanceof NativeArray) {
				System.out.println("TODO NativeArray");
			}
		}

		return _arr;
	}
}
