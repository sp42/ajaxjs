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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.ajaxjs.json.Rhino;
import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;
import sun.org.mozilla.javascript.internal.Undefined;

public class JsonHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(JsonHelper.class);
	
	/**
	 * JS 引擎
	 */
	private ScriptEngine engine;

	/**
	 * 
	 * @param engine JS 引擎
	 */
	public JsonHelper(ScriptEngine engine) {
		this.engine = engine;
	}

	/**
	 * 加载 JS 文件
	 * 
	 * @param engine
	 *            JS 引擎
	 * @param paths
	 *            JS 文件完整路徑
	 */
	public void load(String[] paths) {
		String code;

		for (String path : paths) {
			LOGGER.info("加载 js: {0} 文件", path);

			code = FileUtil.openAsText(path);

			try {
				engine.eval(code);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载单个 js 文件
	 * 
	 * @param path
	 *            JS 文件完整路徑
	 */
	public void load(String path) {
		load(new String[] { path });
	}

	/**
	 * 调用脚本的方法
	 * 
	 * @param method
	 *            js 脚本代码
	 * @param clazz
	 *            目标类型
	 * @param binding
	 *            可以为 null，则表示调用全局方法
	 * @param args
	 *            参数列表
	 * @return JS 运算后的返回值，也可能是 null 没有返回
	 */
	public <T> T call( String method, Class<T> clazz, Object binding, Object... args) {
		Invocable inv = (Invocable) engine; // Invocable 接口是
		// ScriptEngine可选实现的接口。（多态）
		Object result = null;

		try {
			result = binding != null ? inv.invokeMethod(binding, method, args) : inv.invokeFunction(method, args);
		} catch (NoSuchMethodException e) {
			LOGGER.warning("脚本引擎没有{0}() 这个方法", method);
		} catch (ScriptException e) {
			LOGGER.warning("向脚本引擎调用脚本方法异常！方法名称:" + method, e);
		}

		return Util.TypeConvert(result, clazz);
	}

	/**
	 * 写变量 在 Java 中向脚本引擎 (Script Engine) 传递变量，即脚本语言用 java 的变量。当然，使用 eval()
	 * 也可以了。注意可以直接赋值 Java 对象。

	 * @param varName
	 *            变量名
	 * @param obj
	 *            变量值
	 */
	public void put(String varName, Object obj) {
		engine.put(varName, obj);
	}

	/**
	 * 获取 js 的对象，如果最后一个不是对象，返回 Object，之前的为 NativeObject
	 * 
	 * @param namespace
	 *            JS MAP 对象的 key
	 * @return NativeObject 或 Object
	 */
	public Object get(String... namespace) {
		NativeObject obj = (NativeObject) engine.get(namespace[0]);

		for (int i = 1; i < namespace.length; i++) {
			try {
				obj = (NativeObject) obj.get(namespace[i]);
			} catch (ClassCastException e) {
				LOGGER.warning(e);
				return obj.get(namespace[i]);
			}
		}

		return obj;
	}

	/**
	 * 借助 js 序列化对象为 json
	 * 
	 * @param code
	 *            js 对象名称
	 * @return JSON 字符串
	 */
	public String stringify(String code) {
		try {
			return engine.eval("JSON.stringify(" + code + ");").toString();
		} catch (ScriptException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 借助 js 序列化对象为 json
	 * 
	 * @param obj
	 *            NativeArray | NativeObject 均可
	 * @return JSON 字符串
	 */
	public String JSON_Stringify(Object obj) {
		try {
			return call("stringify", String.class, engine.eval("JSON"), obj);
		} catch (ScriptException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify(Map<String, ?> map) {
		if (map == null)
			return null;

		List<String> arr = new ArrayList<>();
		for (String key : map.keySet())
			arr.add('\"' + key + "\":" + obj2jsonVaule(map.get(key)));

		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

//	public static String stringify(List<Map<String, ?>> list) {
//		if (null == list)
//			return null;
//		String[] strArr = new String[list.size()];
//		
//		for (int i = 0; i < list.size(); i ++) {
//			strArr[i] = stringify(list.get(i));
//		}
//
//		return StringUtil.stringJoin(strArr, ",");
//	}
	public static String stringify(List<Map<String, Object>> list) {
		if (null == list)
			return null;
		String[] strArr = new String[list.size()];
		
		for (int i = 0; i < list.size(); i ++) {
			strArr[i] = stringify(list.get(i));
		}
		
		return "[" + StringUtil.stringJoin(strArr, ",") + "]";
	}

	/**
	 * 将 Simple Object 对象转换成JSON格式的字符串:JAVA-->JS
	 * 
	 * @param obj
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify_object(Object obj) {
		if (obj == null)
			return null;
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
				LOGGER.warning(e);
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
	@SuppressWarnings("unchecked")
	public static String obj2jsonVaule(Object value) {
		if (value == null) {
			return "null";
		} else if(value instanceof Double){
			return JSON.double2int((Double)value) + "";
		} else if (value instanceof Boolean || value instanceof Number) {
			return value.toString();
		} else if (value instanceof Date) {
			return '\"' + DateTools.formatDate((Date) value, DateTools.commonDateFormat) + '\"';
		} else if(value instanceof Map){
			return stringify((Map<String, ?>)value);
		} else if(value instanceof List){
			List<?> list = (List<?>)value;
			if(list.size() == 0){
				return "[]";
			} else if (list.get(0) instanceof String) {
				List<String> strList = (List<String>)list;
				StringBuilder sb = new StringBuilder();
				
				for (int i = 0; i < strList.size(); i++) {
					if (i == (strList.size() - 1))sb.append("\"" + strList.get(i) + "\"");
					else sb.append("\"" + strList.get(i) + "\"").append(",");
				}
				
				return '[' + sb.toString() + ']';
			} else if (list.get(0) instanceof Map) {
				List<String> jsonStrList = new ArrayList<>();
				List<Map<String, ?>> maps = (List<Map<String, ?>>) list;
				for (Map<String, ?> map : maps) {
					jsonStrList.add(stringify(map));
				}
				System.out.println("2222222222222222222222");
				return obj2jsonVaule(jsonStrList);
			} else {
				// 未知类型数组，
				return "[]";
			}
			//return stringify((Map<String, ?>)value);
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

	/**
	 * 
	 * 不推荐使用该方法
	 * 
	 * @param value
	 *            Js 里面的值
	 * @return Java 里面的值
	 */
	public static Object jsValue2java(Object value) {
		if (value == null || value instanceof Boolean || value instanceof String || value instanceof Undefined) {
			// js 为 null，所以 java hash 也为null
			// nothing but still value;
		} else if (value instanceof Double) {
			value = JSON.double2int((Double) value);// js number 转换为 short
		} else if (value instanceof NativeObject) {
			value = jsValue2java(value);
		} else if (value instanceof NativeArray) {
			value = (List<?>) value; // 这是规则的情况，数组中每个都是对象，而非
										// string/int/boolean
										// TODO
		} else {
			Rhino.LOGGER.info("未知 JS 类型：" + value.getClass().getName());
		}

		return value;
	}

	/**
	 * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t
	 * 换行用\r TODO 用原生实现
	 * 
	 * @param json
	 *            原 JSON 字符串
	 * @return 格式化后美观的 JSON
	 */
	public static String format(String json) {
		int level = 0;
		StringBuilder str = new StringBuilder();

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (level > 0 && '\n' == str.charAt(str.length() - 1))
				str.append(StringUtil.repeatStr("\t", "", level));

			switch (c) {
			case '{':
			case '[':
				str.append(c + "\n");
				level++;
				break;
			case ',':
				if (json.charAt(i + 1) == '"')
					str.append(c + "\n"); // 后面必定是跟着 key 的双引号，但 其实 json 可以 key
											// 不带双引号的
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

	public ScriptEngine getEngine() {
		return engine;
	}

	public void setEngine(ScriptEngine engine) {
		this.engine = engine;
	}

}
