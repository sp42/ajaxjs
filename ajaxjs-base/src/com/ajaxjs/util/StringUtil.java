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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import com.ajaxjs.util.map.MapHelper;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 字符串相关的工具类
 * 
 * @author frank
 *
 */
public class StringUtil {
	/**
	 * 是否空字符串
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true 表示为为空字符串，否则不为空
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.isEmpty() || str.trim().isEmpty();
	}
	
	/**
	 * 将一个字符串分隔为字符串数组，分隔符 可以是空格、,、|、; 作为分隔符。
	 * 常在读取配置的时候使用。
	 * @param str
	 * @return
	 */
	public static String[] split(String str) {
		return str.split(" |,|\\||;");
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
		if (!CollectionUtil.isNotNull(arr))
			return null;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < arr.length; i++) {
			if (i == (arr.length - 1))
				sb.append(arr[i]);
			else
				sb.append(arr[i]).append(join);
		}

		return new String(sb);
	}
	
	public static String stringJoin(String[] arr) {
		return stringJoin(arr, ", ");
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
		if (!CollectionUtil.isNotNull(arr))
			return null;
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
	 * 重复字符串 repeat 次并以 div 分隔
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
		
		while (i++ < repeat) {
			s.append(str);
			if (i != repeat)
				s.append(div);
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
	 * 使用正则的快捷方式
	 * 
	 * @param regexp
	 *            正则
	 * @param str
	 *            测试的字符串
	 * @return 匹配结果，只有匹配第一个
	 */
	public static String regMatch(String regexp, String str) {
		return regMatch(regexp, str, 0);
	}
	
	/**
	 * 使用正则的快捷方式。可指定分组
	 * 
	 * @param regexp
	 *            正则
	 * @param str
	 *            测试的字符串
	 * @param groupIndex
	 *            分组 id
	 * @return
	 */
	public static String regMatch(String regexp, String str, int groupIndex) {
		Matcher m = Pattern.compile(regexp).matcher(str);
		return m.find() ? m.group(groupIndex) : null;
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
			return URLDecoder.decode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 将字符进行 URL 编码，默认 UTF-8 编码
	 * 
	 * @param str
	 *            正常的 Java 字符串
	 * 
	 * @return 已 URL 编码的字符串
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
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
		return new String(bytes, StandardCharsets.UTF_8);
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
	 * url 网址中文乱码处理。
	 * 如果 Tomcat 过滤器设置了 utf-8 那么这里就不用重复转码了
	 * 
	 * @param str
	 *            通常是 url Query String 参数
	 * @return 中文
	 */
	public static String urlChinese(String str) {
		return byte2String(str.getBytes(StandardCharsets.ISO_8859_1));
	}

	/**
	 * BASE64 编码
	 * 
	 * @param str
	 *            待编码的字符串
	 * @return 已编码的字符串
	 */
	public static String base64Encode(String str) {
		return new BASE64Encoder().encode(str.getBytes());
	}

	/**
	 * BASE64 解码 这里需要强制捕获异常。
	 * 中文乱码：http://s.yanghao.org/program/viewdetail.php?i=54806
	 * 
	 * @param str
	 *            已解码的字符串
	 * @return 已解码的字符串
	 */
	public static String base64Decode(String str) {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = null;

		try {
			bytes = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return byte2String(bytes);
	}
	
	/**
	 * 获取 字符串 md5 哈希值
	 * @param str
	 *            输入的字符串
	 * @return MD5 摘要，返回32位大写的字符串
	 */
	public static String md5(String str) {
		byte[] b = str.getBytes(StandardCharsets.UTF_8);
		
		try {
			b = MessageDigest.getInstance("MD5").digest(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		return DatatypeConverter.printHexBinary(b);
	}
	
	private static final String text = "0123456789abcdefghijklmnopqrstuvwxyz";  
    
	/**
	 * 生成随机密码。可以控制生成的密码长度， 密码由数字和字母组成。
	 * 
	 * @param length
	 *            字符串长度
	 * @return 随机密码
	 */
	public synchronized static String passwordGenerator(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();

		for (int i = 0; i < length; i++)
			sb.append(text.charAt(random.nextInt(text.length())));

		return sb.toString();
	}
    
	/**
	 * 获取一个唯一的主键 id 的代码
	 * 
	 * @return UUID
	 */
	public static String getUUID() {
		return java.util.UUID.randomUUID().toString();
	}

	/**
	 * 去掉“-”符号
	 * 
	 * @param uuid
	 *            UUID 字符串
	 * @return 字符串
	 */
	public static String remove(String uuid) {
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}

	/**
	 * 是否 uuid 的正则表达式
	 */
	private static final String uuid_regExp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

	/**
	 * 判断输入的字符串是否包含 uuid 特征。
	 * 
	 * @param uuid
	 *            输入的字符串
	 * @return true 为 UUID
	 */
	public static boolean isUUID(String uuid) {
		return regMatch(uuid_regExp, uuid) != null;
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

		int styleBegin = content.indexOf("<STYLE"), styleEnd = content.indexOf("</STYLE>") + 8;
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
			if (hex.length() <= 2)
				hex = "00" + hex;
			
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
	 * 
	 * @param buf
	 * @return
	 */
	public static String utf82gb2312(byte buf[]) {
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
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public byte[] gbk2utf8(String str) {
		char c[] = str.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);
			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			
			for (int j = 0; j < len; j++) 
				sb.append("0");
			
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