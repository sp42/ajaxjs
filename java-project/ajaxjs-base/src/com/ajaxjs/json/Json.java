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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.DateTools;
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
	
	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify(Map<String, ?> map) {
		if (com.ajaxjs.util.Util.isNotNull(map)) {
			List<String> arr = new ArrayList<>();
			for (String key : map.keySet())
				arr.add('\"' + key + "\":" + obj2jsonVaule(map.get(key)));
			
			return '{' + StringUtil.stringJoin(arr, ",") + '}';
		} else return null;
	}

	/**
	 * 将 Simple Object 对象转换成JSON格式的字符串:JAVA-->JS
	 * 
	 * @param obj
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify(Object obj) {
		if (obj == null) return null;
		// 检查是否可以交由 JS 转换的类型
		// if(obj instanceof NativeArray || obj instanceof NativeObject)return
		// navtiveStringify(obj);
	
		List<String> arr = new ArrayList<>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
	
			String key = field.getName();
			if (key.indexOf("this$") != -1)
				continue;
	
			Object _obj = null;
			try {
				_obj = field.get(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	
			arr.add('\"' + key + "\":" + obj2jsonVaule(_obj));
		}
	
		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

	/**
	 * 输出符合 JSON 要求的值
	 * 
	 * @param value
	 *            任意一个 Java 值
	 * @return JSON 形式的值
	 */
	public static String obj2jsonVaule(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof Boolean || value instanceof Number) {
			return value.toString();
		} else if (value instanceof Date) {
			return '\"' + DateTools.formatDate((Date) value, DateTools.commonDateFormat) + '\"';
		} else if (value instanceof Object[]) {
			Object[] arr = (Object[]) value;
			String[] strs = new String[arr.length];
			int i = 0;
			for (Object _value : arr)
				strs[i++] = obj2jsonVaule(_value); // 递归调用
	
			return '[' + StringUtil.stringJoin(strs, ",") + ']';
		} else { // String
			return '\"' + value.toString().replace("\\", "\\\\").replace("\"", "\\\"") + '\"';
		}
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

	/**
	 * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t
	 * 换行用\r
	 * TODO 用原生实现
	 * @param json
	 *            原 JSON 字符串
	 * @return 格式化后美观的 JSON
	 */
	public static String format(String json) {
		int level = 0;
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (level > 0 && '\n' == str .charAt(str.length() - 1))
				str.append(StringUtil.repeatStr("\t", "", level));
			
			switch (c) {
				case '{':
				case '[':
					str.append(c + "\n");
					level++;
					break;
				case ',':
					if(json.charAt(i + 1) == '"')
						str.append(c + "\n"); // 后面必定是跟着 key 的双引号，但 其实 json 可以 key 不带双引号的
					break;
				case '}':
				case ']':
					str.append("\n");
					level--;
					str.append(StringUtil.repeatStr("\t", "", level));
					str.append(c);
					break;
				default:
					str.append(c);
					break;
			}
		}
	
		return str.toString();
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
