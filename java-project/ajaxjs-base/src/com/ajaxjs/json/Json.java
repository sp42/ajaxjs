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
package com.ajaxjs.json;

import java.util.List;
import java.util.Map;

import com.ajaxjs.util.StringUtil;

public class Json {
	public static IEngine engine; // 单例
	public static ToJavaType java; // 新建一个 js mapper 专用于 json 转换，以免影响 配置 js runtime
	public static ToJsType js;
	
	static {
		boolean isJDK_8 = System.getProperty("java.version").contains("1.8.");
		
		try {
			Class<?> c = Class.forName(isJDK_8 ? "com.ajaxjs.json.Nashorn" : "com.ajaxjs.json.Rhino");
			engine = (IEngine) c.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		java 	= (ToJavaType)engine; // 多态的应用
		js 		= (ToJsType)engine;
	}
	
	public static Map<String, Object> callExpect_Map(String code, String varName) {
		return java.eval_return_Map(code, varName);
	}
	public static Map<String, Object> callExpect_Map(String code) {
		return java.eval_return_Map(code);
	}
	public static String[] callExpect_StringArray(String code) {
		return java.eval_return_StringArray(code);
	}

	public static Map<String, Object>[] callExpect_MapArray(String code) {
		return java.eval_return_MapArray(code);
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
	
	static {
		final String[] a = {
			"/**",
			 " * Java 类型转换为 js 类型",
			 " * @param v",
			 " * @returns",
			 " */",
			 "function java_value2js_value(v) {",
			 "	if (v == null)",
			 "		return null;",
			 "	if (v.getClass) {",
			 "		switch (v.getClass()) {",
			 "		case java.util.Date:",
			 "			return new Date(v);",
			 "			break;",
			 "		case java.lang.String:",
			 "			return String(v);",
			 "			break;",
			 "		case java.lang.Integer:",
			 "		case java.lang.Number:",
			 "			return Number(v);",
			 "			break;",
			 "		default:",
			 "if(v.getClass().isArray()){if(v[0].getClass() == java.lang.String){",
			 "var arr =[]; for(var i = 0; i < v.length; i++){arr.push(new String(v[i]));};return arr;}}",
			 " else if(v[0].getClass() == java.lang.Integer){var arr =[]; for(var i = 0; i < v.length; i++){arr.push(new Number(v[i]));};return arr;}",
			 "else",
			 "			return String(v);",
			 "		}",
			 "	} else if (Number(v) == v) {",
			 "		return Number(v);",
			 "	} else if (Boolean(v) == v) {",
			 "		return Boolean(v);",
			 "	} else {",
			 "		return null;",
			 "	}",
			 "}",
			 "var hasGetterReg = /^(get|is)/;",
			 "function pojo2json(pojo) {",
			 "	var obj = {};",
			 "	for ( var k in pojo) {",
			 "		var value, hasGetter = hasGetterReg.test(k);",
			 "		if (hasGetter && k != \'getClass\') {",
			 "			value = pojo[k]();",
			 "			var fieldName;",
			 "			if (k[0] == 'g') {",
			 "				fieldName = k.replace(\'get\', \'\');",
			 "			} else {",
			 "				fieldName = k.replace(\'is\', \'\');",
			 "			}",
			 "			// 第一个字母转为小写",
			 "			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1);",
			 "			obj[fieldName] = java_value2js_value(value);",
			 "		}",
			 "	}",
			 "	return obj;",
			 "}",
			 "function test(list) {",
			 "	var j = list.size(), str = new Array(j);",
			 "	for (var i = 0; i < j; i++) {",
			 "		var pojo = list.get(i);",
			 "		str[i] = pojo2json(pojo);",
			 "	}",
			 "	return JSON.stringify(str);",
			 "}; function singlePojo(pojo){return JSON.stringify(pojo2json(pojo));}"
		};
		
		final String jsStr = StringUtil.stringJoin(a, "\n");
		
		engine.eval(jsStr);
		
//		try {
//			js.load(Util.getClassFolder_FilePath(Java2Json.class, "js.js"));
//		} catch (ScriptException | IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public static String list(List<?> list) {
		return list == null || list.isEmpty() || list.size() == 0 ? "null" : engine.call("test", String.class, null, list);
	}
	
	public static String singlePojo(Object obj) {
		return engine.call("singlePojo", String.class, null, obj);
	}

	/**
	 * 借助 Rhino 序列化
	 * 
	 * @param obj
	 *            NativeArray | NativeObject 均可
	 * @return JSON 字符串
	 */
	public static String JSON_stringify(Object obj) {
		return js.JSON_Stringify(obj);
	}
}
