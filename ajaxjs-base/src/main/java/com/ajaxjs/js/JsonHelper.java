/**
 * Copyright Sp42 frank@ajaxjs.com
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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.js.jsonparser.syntax.FMS;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MapTool;

/**
 * 序列化/反序列化 JSON
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class JsonHelper {
	/**
	 * 解析 JSON 为 Map 或 List
	 * 
	 * @param str JSON 字符串
	 * @return Map 或 List
	 */
	public static Object parse(String str) {
		return new FMS(str).parse();
	}

	/**
	 * 解析 JSON 字符串为 Map
	 * 
	 * @param str JSON 字符串
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseMap(String str) {
		return (Map<String, Object>) parse(str);
	}

	/**
	 * 解析 JSON 字符串为 List
	 * 
	 * @param str 字符串
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> parseList(String str) {
		return (List<Map<String, Object>>) parse(str);
	}

	/**
	 * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t 换行用\r
	 * 
	 * @param json 原 JSON 字符串
	 * @return 格式化后美观的 JSON
	 */
	public static String format(String json) {
		int level = 0;
		StringBuilder str = new StringBuilder();

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (level > 0 && '\n' == str.charAt(str.length() - 1))
				str.append(CommonUtil.repeatStr("\t", "", level));

			switch (c) {
			case '{':
			case '[':
				str.append(c + "\n");
				level++;
				break;
			case ',':
				if (json.charAt(i + 1) == '"')
					str.append(c + "\n"); // 后面必定是跟着 key 的双引号，但 其实 json 可以 key 不带双引号的
				break;
			case '}':
			case ']':
				str.append("\n");
				level--;
				str.append(CommonUtil.repeatStr("\t", "", level));
				str.append(c);
				break;
			default:
				str.append(c);
				break;
			}
		}

		return str.toString();
	}

	/**
	 * 删除注释
	 * 
	 * @param str 待处理的字符串
	 * @return 删除注释后的字符串
	 */
	public static String removeComemnt(String str) {
		return str.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
	}
	
	/**
	 * 输出到 JSON 文本时候的换行
	 * 
	 * @param str
	 * @return
	 */
	public static String jsonString_covernt(String str) {
		return str.replace("\r\n", "\\n");
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String javaValue2jsonValue(String str) {
		return str.replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\\t");
	}

	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map 输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyMap(Map<?, ?> map) {
		if (map == null)
			return null;
	
		if (map.size() == 0)
			return "{}";
	
		List<String> arr = new ArrayList<>();
		for (Object key : map.keySet())
			arr.add('\"' + key.toString() + "\":" + toJson(map.get(key)));
	
		return '{' + String.join(",", arr) + '}';
	}

	/**
	 * 对 Object 尝试类型检测，将其合适地转换为 JSON 字符串返回。
	 * 
	 * @param obj 任意对象
	 * @return JSON 字符串
	 */
	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return '\"' + obj.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + '\"';
		} else if (obj instanceof Double) {
			return obj + "";
		} else if (obj instanceof Boolean || obj instanceof Number) {
			return obj.toString();
		} else if (obj instanceof Date) {
			return '\"' + CommonUtil.SimpleDateFormatFactory(CommonUtil.commonDateFormat).format((Date) obj) + '\"';
	
		} else if (obj.getClass() == Integer[].class) {
			return jsonArr((Integer[]) obj, v -> v + "");
		} else if (obj.getClass() == int[].class) {
			Integer[] arr = Arrays.stream((int[]) obj).boxed().toArray(Integer[]::new);
			return jsonArr(arr, v -> v + "");
		} else if (obj instanceof Long[]) {
			return jsonArr((Long[]) obj, v -> v.toString());
		} else if (obj instanceof long[]) {
			Long[] arr = Arrays.stream((long[]) obj).boxed().toArray(Long[]::new);
			return jsonArr(arr, v -> v.toString());
		} else if (obj instanceof String[]) {
			return jsonArr((String[]) obj, v -> "\"" + v + "\"");
		} else if (obj instanceof Map) {
			return stringifyMap((Map<?, ?>) obj);
		} else if (obj instanceof Map[]) {
			return jsonArr((Map<?, ?>[]) obj, JsonHelper::stringifyMap);
	
		} else if (obj instanceof BaseModel) {
			return beanToJson((BaseModel) obj);
		} else if (obj instanceof BaseModel[]) {
			return jsonArr((BaseModel[]) obj, JsonHelper::beanToJson);
	
		} else if (obj instanceof List) {
			List<?> list = (List<?>) obj;
	
			if (list.size() > 0) {
				if (list.get(0) instanceof Integer) {
					return toJson(list.toArray(new Integer[list.size()]));
				} else if (list.get(0) instanceof String) {
					return toJson(list.toArray(new String[list.size()]));
				} else if (list.get(0) instanceof Map) { // Map 类型的输出
					return toJson(list.toArray(new Map[list.size()]));
				} else if (list.get(0) instanceof BaseModel) { // Bean
					return toJson(list.toArray(new BaseModel[list.size()]));
				}
			} else {
				return "[]";
			}
		} else if (obj instanceof Object[]) {
			return jsonArr((Object[]) obj, v -> toJson(v));
		} else if (obj instanceof Object) {
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
	
				arr.add('\"' + key + "\":" + toJson(_obj));
			}
	
			return '{' + String.join(",", arr) + '}';
		} else {
			throw new RuntimeException("不支持数据类型");
		}
	
		return null;
	}

	/**
	 * 输入任意类型数组，在 fn 作适当的转换，返回 JSON 字符串
	 * 
	 * @param o 数组
	 * @param fn 元素处理器，返回元素 JSON 字符串
	 * @return 数组的 JSON 字符串
	 */
	public static <T> String jsonArr(T[] o, Function<T, String> fn) {
		if (o.length == 0)
			return "[]";
	
		StringBuilder sb = new StringBuilder();
	
		for (int i = 0; i < o.length; i++) {
			sb.append(fn.apply((T) o[i]));
			if (i != (o.length - 1))
				sb.append(", ");
		}
	
		return '[' + sb.toString() + ']';
	}

	/**
	 * 
	 * @param list
	 * @param fn
	 * @return
	 */
	static <T> String eachList(List<T> list, Function<T, String> fn) {
		StringBuilder sb = new StringBuilder();
	
		for (int i = 0; i < list.size(); i++) {
			sb.append(fn.apply(list.get(i)));
			if (i != (list.size() - 1))
				sb.append(", ");
		}
	
		return '[' + sb.toString() + ']';
	}

	/**
	 * Bean 转换为 JSON 字符串
	 * 
	 * 传入任意一个 Java Bean 对象生成一个指定规格的字符串
	 * 
	 * @param bean bean对象
	 * @return String "{}"
	 */
	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
	
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = MapTool.objectToJson(props[i].getName());
					String value = toJson(props[i].getReadMethod().invoke(bean));
	
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
	
		return json.toString();
	}
	
	// --------------------------------------------------------------------------------------------------
	// --------------------------------------------JSON output-------------------------------------------
	// --------------------------------------------------------------------------------------------------

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 输出 JSON OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonOk(String msg) {
		return String.format(json_ok, msg);
	}

	/**
	 * 输出 JSON No OK
	 * 
	 * @param msg 输出信息
	 * @return JSON 字符串
	 */
	public static String jsonNoOk(String msg) {
		return String.format(json_not_ok, msg);
	}

	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String json_ok_extension = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";
	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "json::{\"isOk\": true, \"msg\" : \"%s\"}";
}