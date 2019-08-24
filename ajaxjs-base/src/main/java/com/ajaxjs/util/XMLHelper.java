package com.ajaxjs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://blog.csdn.net/axman/article/details/420910
 * 
 * @author Administrator
 *
 */
public class XMLHelper {
	private String xmlString;

	/**
	   * 传入xml的字符串内容,对于InputStream,Reader对象请转换为String对象后传入构造方法.
	   * @param xmlString String
	   * @throws IllegalArgumentException
	   */
	public XMLHelper(String xmlString) throws IllegalArgumentException {
		if (xmlString == null || xmlString.length() == 0)
			throw new IllegalArgumentException("Input string orrer!");
		this.xmlString = xmlString;
	}

	/**
	 * 在文档中搜索指定的元素,返回符合条件的元素数组.
	 * @param tagName String
	 * @return String[]
	 */
	public String[] getElementsByTag(String tagName) {
		Pattern p = Pattern.compile("<" + tagName + "[^>]*?((>.*?</" + tagName + ">)|(/>))");
		Matcher m = p.matcher(this.xmlString);
		ArrayList<String> al = new ArrayList<String>();
		while (m.find())
			al.add(m.group());
		String[] arr = al.toArray(new String[al.size()]);
		al.clear();
		return arr;
	}

	/**
	 * 用xpath模式提取元素,以#为分隔符
	 * 如 ROOT#PARENT#CHILD表示提取ROOT元素下的PARENT元素下的CHILD元素
	 * @param singlePath String
	 * @return String
	 */
	public String getElementBySinglePath(String singlePath) {
		String[] path = singlePath.split("#");
		String lastTag = path[path.length - 1];
		String tmp = "(<" + lastTag + "[^>]*?((>.*?</" + lastTag + ">)|(/>)))";

		// 最后一个元素,可能是<x>v</x>形式或<x/>形式
		for (int i = path.length - 2; i >= 0; i--) {
			lastTag = path[i];
			tmp = "<" + lastTag + ">.*" + tmp + ".*</" + lastTag + ">";
		}

		Pattern p = Pattern.compile(tmp);
		Matcher m = p.matcher(this.xmlString);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * 用xpath模式提取元素从多重元素中获取指批定元素,以#为分隔符
	 * 元素后无索引序号则默认为0: ROOT#PARENT[2]#CHILD[1]
	 * @param singlePath String
	 * @return String
	 */
	public String getElementByMultiPath(String singlePath) {
		try {
			String[] path = singlePath.split("#");
			String input = this.xmlString;
			String[] ele = null;
			for (int i = 0; i < path.length; i++) {
				Pattern p = Pattern.compile("(//w+)(//[(//d+)//])?");
				Matcher m = p.matcher(path[i]);
				if (m.find()) {
					String tagName = m.group(1);
					System.out.println(input + "----" + tagName);
					int index = (m.group(3) == null) ? 0 : new Integer(m.group(3)).intValue();
					ele = getElementsByTag(input, tagName);
					input = ele[index];
				}
			}
			return input;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 在给定的元素中搜索指定的元素,返回符合条件的元素数组.对于不同级别的同名元素限制作用,即可以
	 * 搜索元素A中的子元素C.而对于元素B中子元素C则过虑,通过多级限定可以准确定位.
	 * @param parentElementString String
	 * @param tagName String
	 * @return String[]
	 */
	public static String[] getElementsByTag(String parentElementString, String tagName) {
		Pattern p = Pattern.compile("<" + tagName + "[^>]*?((>.*?</" + tagName + ">)|(/>))");
		Matcher m = p.matcher(parentElementString);
		ArrayList<String> al = new ArrayList<String>();
		while (m.find())
			al.add(m.group());
		String[] arr = al.toArray(new String[al.size()]);
		al.clear();
		return arr;
	}

	/**
	 * 从指定的父元素中根据xpath模式获取子元素,singlePath以#为分隔符
	 * 如 ROOT#PARENT#CHILD表示提取ROOT元素下的PARENT元素下的CHILD元素
	 * @param parentElementString String
	 * @param singlePath String
	 * @return String
	 */
	public static String getElementBySinglePath(String parentElementString, String singlePath) {
		String[] path = singlePath.split("#");
		String lastTag = path[path.length - 1];

		String tmp = "(<" + lastTag + "[^>]*?((>.*?</" + lastTag + ">)|(/>)))";

		// 最后一个元素,可能是<x>v</x>形式或<x/>形式
		for (int i = path.length - 2; i >= 0; i--) {
			lastTag = path[i];
			tmp = "<" + lastTag + ">.*" + tmp + ".*</" + lastTag + ">";
		}
		Pattern p = Pattern.compile(tmp);
		Matcher m = p.matcher(parentElementString);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * 用xpath模式提取元素从指定的多重元素中获取指批定元素,以#为分隔符
	 * @param parentElementString String
	 * @param singlePath String
	 * @return String
	 */
	public static String getElementByMultiPath(String parentElementString, String singlePath) {
		String[] path = singlePath.split("#");
		String input = parentElementString;
		String[] ele = null;

		try {
			for (int i = 0; i < path.length; i++) {
				Pattern p = Pattern.compile("(//w+)(//[(//d+)//])?");
				Matcher m = p.matcher(path[i]);
				if (m.find()) {
					String tagName = m.group(1);
					int index = (m.group(3) == null) ? 0 : new Integer(m.group(3)).intValue();
					ele = getElementsByTag(input, tagName);
					input = ele[index];
				}
			}
			return input;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	* 在给定的元素中获取所有属性的集合.该元素应该从getElementsByTag方法中获取
	* @param elementString String
	* @return HashMap
	*/
	public HashMap<String, String> getAttributes(String elementString) {
		HashMap<String, String> hm = new HashMap<>();
		Pattern p = Pattern.compile("<[^>]+>");
		Matcher m = p.matcher(elementString);
		String tmp = m.find() ? m.group() : "";

		// p = Pattern.compile("(//w+)//s*=//s*/"([^/"]+)/"");
		m = p.matcher(tmp);
		while (m.find()) {
			hm.put(m.group(1).trim(), m.group(2).trim());
		}
		return hm;
	}

	/**
	* 在给定的元素中获取指定属性的值.该元素应该从getElementsByTag方法中获取
	* @param elementString String
	* @param attributeName String
	* @return String
	*/
	public static String getAttribute(String elementString, String attributeName) {
		// HashMap<String, String> hm = new HashMap<>();
		Pattern p = Pattern.compile("<[^>]+>");
		Matcher m = p.matcher(elementString);
		String tmp = m.find() ? m.group() : "";
		// p = Pattern.compile("(//w+)//s*=//s*/"([^/"]+)/"");
		m = p.matcher(tmp);
		while (m.find()) {
			if (m.group(1).trim().equals(attributeName))
				return m.group(2).trim();
		}
		return "";
	}

	/**
	 * 获取指定元素的文本内容
	 * @param elementString String
	 * @return String
	 */
	public static String getElementText(String elementString) {
		Pattern p = Pattern.compile(">([^<>]*)<");
		Matcher m = p.matcher(elementString);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public static void main(String[] args) {
		new XMLHelper("<ROOT>sss <PARENT>sss <CHILD>aaaa</CHILD>ss </PARENT>sss </ROOT>")
				.getElementByMultiPath("ROOT[0]#PARENT#CHILD");
//		System.out.println(child);
	}
}
