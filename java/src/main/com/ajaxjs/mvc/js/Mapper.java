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
package com.ajaxjs.mvc.js;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.util.LogHelper;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

/**
 * 新建一个 js mapper 专用于 json 转换，以免影响 配置 js runtime
 * @author Frank Cheung
 *
 */
public class Mapper extends Java2Json{
	private static final LogHelper LOGGER = LogHelper.getLog(Mapper.class);

	/**
	 * JS Object 对象转换到 Java Hash 对象
	 * 
	 * @param obj
	 *            JS 里面的 Map
	 * @return Java 里面的 Map
	 */
	public static Map<String, Object> NativeObject2Map(NativeObject obj) {
		Map<String, Object> hash = null;

		if (obj != null) {
			hash = new HashMap<>();

			for (Object id : obj.getAllIds()) {// 遍历对象
				String newId = id.toString();
				Object value = obj.get(newId, obj);
				hash.put(newId, jsValue2java(value));
			}
		}

		return hash;
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
		if (arr != null && arr.size() > 0) { // NativeArray.isEmpty 有 bug，
//			Not real Map!
//			return (Map<String, Object>[])arr.toArray(new Map[arr.size()]);

			Map<String, Object>[] _arr = new HashMap[arr.size()]; // java 不支持泛型数组
			int i = 0;
			for (Object obj : arr) { // JDK7 supports directly
				if (obj instanceof NativeObject) {
					_arr[i++] = NativeObject2Map((NativeObject) obj);
				} else if (obj instanceof NativeArray) {
					LOGGER.info("TODO NativeArray");
				}
			}

			return _arr;
		} else
			return null;
	}
	
//	/**
//	 * JS Array 对象转换到 Java Map Array 对象
//	 * @param arr
//	 * @return
//	 */
//	public static Map<String, Object>[] NativeArray2MapArray(Object arr){
//		return NativeArray2MapArray((NativeArray)arr);
//	}

	/**
	 * JS Array 对象转换到 Java String Array 对象
	 * 
	 * @param arr
	 *            JS 里面的数组，为 String 组成
	 * @return Java 里面的 Map[]
	 */
	public static String[] NativeArray2StringArray(NativeArray arr) {
		if (arr != null && arr.size() > 0) {
			return (String[]) arr.toArray(new String[arr.size()]);
		} else
			return null;
	}
	
//	/**
//	 * JS Array 对象转换到 Java String Array 对象
//	 * @param arr
//	 * @return
//	 */
//	public static String[] NativeArray2StringArray(Object arr){
//		return NativeArray2StringArray((NativeArray)arr);
//	}
	
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
		if (arr != null && arr.size() > 0) {
			return (T[]) arr.toArray((T[]) Array.newInstance(clazz, arr.size()));
		} else
			return null;
	}
	 
	private static Object jsValue2java(Object value) {
		if(value == null || value instanceof Boolean || value instanceof String){
			// js 为 null，所以 java hash 也为null
			// nothing but still value;
		}else if(value instanceof Double){
			value = ((Double)value).intValue();// js number 转换为  short
		}else if(value instanceof NativeObject){
			//value = jsValue2java(value);
		}else if(value instanceof NativeArray){
			value = NativeArray2MapArray((NativeArray)value); // 这是规则的情况，数组中每个都是对象，而非 string/int/boolean @todo
		}else{
			LOGGER.info("未知 JS 类型：" + value.getClass().getName());
		}
		
		return value;
	}
	
	public static Map<String, Object> callExpect_Map(String code) {
		return js.eval_return_Map(code);
	}

	public static Map<String, Object> callExpect_Map(String code, String varName) {
		return js.eval_return_Map(code, varName);
	}

	public static String[] callExpect_StringArray(String code) {
		return js.eval_return_StringArray(code);
	}

	public static Map<String, Object>[] callExpect_MapArray(String code) {
		return js.eval_return_MapArray(code);
	}
	
//	/**
//	 * 输入 JSON 字符串，返回 Map
//	 * @param jsonStr
//	 * @return
//	 */
//	public Map<String, Object> parse(String jsonStr){
//		return callExpect_Map(jsonStr);
//	}
//	
//	/**
//	 *  输入 JSON 字符串，返回 Map 数组
//	 * @param jsonStr
//	 * @return
//	 */
//	public Map<String, Object>[] parseArr(String jsonStr){
//		return callExpect_MapArray(jsonStr);
//	}
	
}