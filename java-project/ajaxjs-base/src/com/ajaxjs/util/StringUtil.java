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

package com.ajaxjs.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关的工具类
 * @author frank
 *
 */
public class StringUtil {
	/**
	 * 空字符串常量
	 */
	public static final String emptyString = "";
	
	public static final String encoding_UTF8 = "UTF-8";
	
	/**
	 * 是否空字符串
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true 表示为为空字符串，否则不为空
	 */
	public static boolean isEmptyString(String str) {
		if (str == null || str.isEmpty() || str.length() == 0
				|| str.trim().isEmpty() || emptyString.equals(str)
				|| emptyString.equals(str.trim()))
			return true;

		return false;
	}
	
	/**
	 * 如果字符串为 null 返回空字符串，否则为字符串本身
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return 字符串本身或者 null
	 */
	public static String emptyStringIfNull(String str) {
		return str == null ? emptyString : str;
	}
	
	/**
	 * Java String 有 split 却没有 join，这里实现一个
	 * 
	 * @param arr
	 *            输入的字符串数组
	 * @param join
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String stringJoin(String[] arr, String join) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < arr.length; i++) {
			if (i == (arr.length - 1))sb.append(arr[i]);
			else sb.append(arr[i]).append(join);
		}

		return new String(sb);
	}
	
	/**
	 * Java String 有 split 却没有 join，这里实现一个
	 * 
	 * @param arr
	 *            输入的字符串列表
	 * @param join
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String stringJoin(List<String> arr, String join) {
		return stringJoin(arr.toArray(new String[arr.size()]), join);
	}
	
	/**
	 * 也是 join，不过输入的参数不是数组而是 hash
	 * 
	 * @param map
	 *            输入的 map
	 * @param div
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String HashJoin(Map<String, String> map, String div) {
		return MapHelper.join(map, div);
	}
	
	/**
	 * 重载版本
	 * 
	 * @param map
	 *            输入的 map
	 * @param div
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String HashJoin(Map<String, String> map, char div) {
		return MapHelper.join(map, div + "");
	}
	
	/**
	 * 重复字符串 str repeat 次并以 div 分隔
	 * 
	 * @param str
	 *            要重复的字符串
	 * @param div
	 *            字符串之间的分隔符
	 * @param repeat
	 *            重复次数
	 * @return 结果
	 */
	public static String repeatStr(String str, String div, int repeat) {
		StringBuilder s = new StringBuilder();
		int i = 0;
		while(i++ < repeat){
			s.append(str);
			if(i != repeat) s.append(div);
		}
		
		return s.toString();
	}
	
	/**
	 * 输入 a，看是否包含另一个字符串 b，忽略大小写。
	 * 
	 * @param a
	 *            输入字符串 a
	 * @param b
	 *            另一个字符串 b
	 * @return true 表示包含
	 */
	public static boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}
	
	/**
	 * 将 URL 编码的字符还原，默认 UTF-8 编码
	 * 
	 * @param str
	 *            已 URL 编码的字符串
	 * @return 正常的 Java 字符串
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, encoding_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将 URL 编码的字符还原，默认 UTF-8 编码
	 * 
	 * @param str
	 *            已 URL 编码的字符串
	 * @return 正常的 Java 字符串
	 */
	public static String urlEecode(String str) {
		try {
			return URLEncoder.encode(str, encoding_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 字节转为 UTF-8 字符串
	 * 
	 * @param bytes
	 *            输入字符串的字节数组
	 * @return UTF-8 字符串
	 */
	public static String byte2String(byte[] bytes) {
		try {
			return new String(bytes, encoding_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 字节转为 UTF-8 字符串
	 * 
	 * @param str
	 *            输入的字符串
	 * @return UTF-8 字符串
	 */
	public static String byte2String(String str) {
		return byte2String(str.getBytes());
	}
	
	/**
	 * 使用正则的快捷方式
	 * 
	 * @param regexp
	 *            正则
	 * @param str
	 *            测试的字符串
	 * @return 匹配结果，只有匹配第一个
	 */
	public static String regMatch(String regexp, String str) {
		Matcher m = Pattern.compile(regexp).matcher(str);
		return m.find() ? m.group() : null;
	}

	/**
	 * word转换html时，会留下很多格式，有些格式并不是我们所需要的， 然而这些格式比真正的文章内容还要多，严重影响页面的加载速度，
	 * 因此就需要找个一个好的解决方案把这些多余的格式个去掉。
	 * 
	 * @param content
	 *            待清理内容
	 * @return 已清理内容
	 */
	public static String clearWordFormat(String content) {
		// 把<P></P>转换成</div></div>保留样式
		// content = content.replaceAll("(<P)([^>]*>.*?)(<\\/P>)",
		// "<div$2</div>");
		// 把<P></P>转换成</div></div>并删除样式
		content = content.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");
		// 删除不需要的标签
		content = content.replaceAll(
				"<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|[ovwxpOVWXP]:\\w+)[^>]*?>", "");
		// 删除不需要的属性
		content = content.replaceAll(
				"<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|[ovwxpOVWXP]:\\w+)=(?:'[^']*'|\"\"[^\"\"]*\"\"|[^>]+)([^>]*)>",
				"<$1$2>");
		// 删除<STYLE TYPE="text/css"></STYLE>及之间的内容
	
		int styleBegin = content.indexOf("<STYLE");
		int styleEnd = content.indexOf("</STYLE>") + 8;
		String style = content.substring(styleBegin, styleEnd);
		content = content.replace(style, "");
	
		return content;
	}
	
	/**
	 * unicode 编码
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 已编码的字符串
	 */
	public static String encodeUnicode(String str) {
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < chars.length; i++) {
			String hex = Integer.toHexString(chars[i]);
			if (hex.length() <= 2) {
				hex = "00" + hex;
			}
			sb.append("\\u" + hex);
		}
		
		return sb.toString();
	}

	/**
	 * unicode 解码
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 已解码的字符串
	 */
	public static String decodeUnicode(String str) {
		int start = 0, end = 0;
		StringBuilder buffer = new StringBuilder();
		
		while (start > -1) {
			end = str.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = str.substring(start + 2, str.length());
			} else {
				charStr = str.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		
		return buffer.toString();
	}
	
	/**
	 * unicode 解码（版本二）
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 已解码的字符串
	 */
	public static String decodeUnicode2(String str) {
		char aChar;
		int len = str.length();
		StringBuilder outBuffer = new StringBuilder(len);
		
		for (int x = 0; x < len;) {
			aChar = str.charAt(x++);
			if (aChar == '\\') {
				aChar = str.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = str.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';

					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}

		return outBuffer.toString();
	}   

	private static int by2int(int b) {
		return b & 0xff;
	}

	/**
	 * 过滤utf8 特殊字符的方式处理如●■★
	 * @param buf
	 * @return
	 */
	public static String UTF82GB2312(byte buf[]) {
		int len = buf.length;
		StringBuffer sb = new StringBuffer(len / 2);
		for (int i = 0; i < len; i++) {
			if (by2int(buf[i]) <= 0x7F) {
				sb.append((char) buf[i]);
			} else if (by2int(buf[i]) <= 0xDF && by2int(buf[i]) >= 0xC0) {
				int bh = by2int(buf[i] & 0x1F), bl = by2int(buf[++i] & 0x3F);
				bl = by2int(bh << 6 | bl);
				bh = by2int(bh >> 2);
				int c = bh << 8 | bl;
				sb.append((char) c);
			} else if (by2int(buf[i]) <= 0xEF && by2int(buf[i]) >= 0xE0) {
				int bh = by2int(buf[i] & 0x0F), bl = by2int(buf[++i] & 0x3F), bll = by2int(buf[++i] & 0x3F);
				bh = by2int(bh << 4 | bl >> 2);
				bl = by2int(bl << 6 | bll);
				int c = bh << 8 | bl;
				// 空格转换为半角
				if (c == 58865)
					c = 32;

				sb.append((char) c);
			}
		}
		return sb.toString();
	} 
	
	public byte[] gbk2utf8(String chenese) {
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);

			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");

			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);

			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];

		}
		return fullByte;
	}
}
