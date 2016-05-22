package com.ajaxjs.json;

import com.ajaxjs.util.StringUtil;

public class JsonUtil {
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
}
