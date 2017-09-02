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
package com.ajaxjs.js;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajaxjs.js.jsonparser.syntax.FMS;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Value;
import com.ajaxjs.util.reflect.BeanUtil;

/**
 * 序列化/反序列化 JSON
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class JsonHelper {
	/**
	 * 解析 JSON 为 Map 或 List
	 * 
	 * @param str
	 *            JSON 字符串
	 * @return Map 或 List
	 */
	public static Object parse(String str) {
		Object obj = new FMS(str).parse();
		return obj;
	}

	/**
	 * 解析 JSON 字符串为 Map
	 * 
	 * @param str
	 *            JSON 字符串
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseMap(String str) {
		return (Map<String, Object>) parse(str);
	}

	/**
	 * 解析 JSON 字符串为 List
	 * 
	 * @param strJSON
	 *            字符串
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> parseList(String str) {
		return (List<Map<String, Object>>) parse(str);
	}

	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyMap(Map<String, ?> map) {
		if (map == null)
			return null;

		List<String> arr = new ArrayList<>();
		for (String key : map.keySet())
			arr.add('\"' + key + "\":" + Value.obj2jsonVaule(map.get(key)));

		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

	/**
	 * 输入一个 List<Map<String, Object>>，将其转换为 JSON Str
	 * 
	 * @param list
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyListMap(List<Map<String, Object>> list) {
		if (null == list)
			return null;

		String[] str = new String[list.size()];

		for (int i = 0; i < list.size(); i++)
			str[i] = stringifyMap(list.get(i));

		return "[" + StringUtil.stringJoin(str, ",") + "]";
	}

	/**
	 * 
	 * @param bean
	 *            BEAN 对象
	 * @return
	 */
	public static String bean2json(Object bean) {
		return stringifyMap(BeanUtil.bean2Map(bean));
	}

	public static String beans2json(List<Object> beans) {
		StringBuilder sb = new StringBuilder();
		for (Object bean : beans) {

			System.out.println(bean.getClass());
			System.out.println(BeanUtil.bean2Map(bean));

			sb.append(stringifyMap(BeanUtil.bean2Map(bean)));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param json
	 * @param clz
	 * @return
	 */
	public static <T> T json2bean(String json, Class<T> clz) {
		Map<String, Object> map = parseMap(json);
		return BeanUtil.map2Bean(map, clz, true);
	}

	/**
	 * 将 Simple Object 对象转换成 JSON 格式的字符串:JAVA-->JS
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
				e.printStackTrace();
			}

			arr.add('\"' + key + "\":" + Value.obj2jsonVaule(_obj));
		}

		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

	/**
	 * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t
	 * 换行用\r
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

	/**
	 * 删除注释
	 * 
	 * @param str
	 * @return
	 */
	public static String removeComemnt(String str) {
		return str.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
	}
}