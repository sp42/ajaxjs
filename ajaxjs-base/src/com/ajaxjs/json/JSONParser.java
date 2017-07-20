package com.ajaxjs.json;

import com.ajaxjs.json.syntax.FMS;

/**
 * 主要的方法入口 parse()。 还有其他的一些工具方法。
 */
public class JSONParser {
	/**
	 * 解析 JSON 为 Map 或 List
	 * 
	 * @param str
	 *            JSON 字符串
	 * @return Map 或 List
	 */
	public static Object parse(String str) {
		return new FMS(str).parse();
	}

	/**
	 * 是否空格字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	/**
	 * 是否字符或者下划线
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetterUnderline(char c) {
		return (c >= 'a' && c <= 'z') || c == '_';
	}

	/**
	 * 是否数字字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * 是否数字字符或小数
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isDecimal(char c) {
		return isNum(c) || c == '.';
	}

	/**
	 * 是否字符或者下划线或下划线
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNumLetterUnderline(char c) {
		return isLetterUnderline(c) || isNum(c) || c == '_';
	}

	/**
	 * 转义
	 * 
	 * @param str
	 * @return
	 */
	public static String unescape(String str) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\\') {
				c = str.charAt(++i);// 游标前进一个字符

				switch (c) {
				case '"':
					sb.append('"');
					break;
				case '\\':
					sb.append('\\');
					break;
				case '/':
					sb.append('/');
					break;
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'u':
					String hex = str.substring(i + 1, i + 5);
					sb.append((char) Integer.parseInt(hex, 16));
					i += 4;
					break;
				default:
					throw new RuntimeException("“\\”后面期待“\"\\/bfnrtu”中的字符，结果得到“" + c + "”");
				}
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}
}
