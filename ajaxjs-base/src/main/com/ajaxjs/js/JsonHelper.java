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

import java.util.List;
import java.util.Map;

import com.ajaxjs.js.jsonparser.syntax.FMS;
import com.ajaxjs.util.CommonUtil;

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
	 * 
	 * @param str
	 * @return
	 */
	public static String javaValue2jsonValue(String str) {
		return str.replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\\t");
	}
}