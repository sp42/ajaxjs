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
import java.util.List;

import com.ajaxjs.javascript.RhinoEngine;
import com.ajaxjs.util.StringUtil;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;


/**
 * Java对象向 JS 的转换
 * @author Frank Cheung
 *
 */
public class Java2Json {
	
	public static RhinoEngine js = new RhinoEngine(); // 单例

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
		
		js.eval(jsStr);
		
//		try {
//			js.load(Util.getClassFolder_FilePath(Java2Json.class, "js.js"));
//		} catch (ScriptException | IOException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 借助 Rhino 序列化
	 * 
	 * @param obj
	 *            NativeArray | NativeObject 均可
	 * @return JSON 字符串
	 */
	public static String JSON_stringify(Object obj) {
		return js.call("stringify", String.class, js.eval("JSON"), obj);
	}
	
	public static String list(List<?> list) {
		return list == null || list.isEmpty() || list.size() == 0 ? "null" : js.call("test", String.class, null, list);
	}
	
	public static String singlePojo(Object obj) {
		return js.call("singlePojo", String.class, null, obj);
	}
	
	/**
	 * 借助 Rhino 序列化 JS数组，返回 JSON 字符串
	 * 
	 * @param jsonObj
	 *            JS 数组
	 * @return JSON 字符串
	 */
	public static String navtiveStringify(NativeArray jsonObj) {
		return JSON_stringify(jsonObj);
	}

	/**
	 * 借助 Rhino 序列化 JS 对象，返回 JSON 字符串
	 * 
	 * @param jsonObj
	 *            JS 里面的 Map
	 * @return JSON 字符串
	 */
	public static String navtiveStringify(NativeObject jsonObj) {
		return JSON_stringify(jsonObj);
	}
}
