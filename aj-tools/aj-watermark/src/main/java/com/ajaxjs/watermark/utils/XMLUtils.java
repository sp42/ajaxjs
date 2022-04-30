package com.ajaxjs.watermark.utils;

import java.util.List;

import org.dom4j.Element;

public class XMLUtils {

	/**
	 * 为某个父节点新增新的子节点，前提是该父节点没有与子节点一样的节点
	 * 
	 * @param root       根节点，即将添加子节点的节点
	 * @param label      新增节点的标签名称
	 * @param text       文本中的内容
	 * @param properties 新增的节点的属性-值，可有多个
	 * @return 是否存在修改
	 */
	public static Boolean addElementToElement(Element root, String label, String text, Pair... properties) {
		List<Element> elements = root.elements(label);

		// 判断是否存在该元素
		boolean hasElement = false;
		for (Element element : elements) {
			boolean isSameElement = true;

			for (Pair pair : properties) {
				String[] split = pair.getKey().split(":");
				String key = split[split.length - 1];

				if (!pair.getValue().equals(element.attributeValue(key))) {
					isSameElement = false;
					break;
				}
			}

			if (isSameElement)
				hasElement = true;
		}

		if (hasElement) {
			Element element = root.element(label);
			if (element != null && text != null) {
				element.setText(text);
				return true;
			}

			return false;
		}

		// 创建新的节点
		Element element = root.addElement(label);

		if (text != null)
			element.setText(text);

		// 为新节点添加属性值
		for (Pair pair : properties)
			element.addAttribute(pair.getKey(), pair.getValue());

		return true;
	}

	/**
	 * 为某个父节点新增新的子节点，前提是该父节点没有与子节点一样的节点
	 * 
	 * @param root       根节点，即将添加子节点的节点
	 * @param label      新增节点的标签名称
	 * @param properties 新增的节点的属性-值，可有多个
	 * @return
	 */
	public static Boolean addElementToElement(Element root, String label, Pair... properties) {
		return addElementToElement(root, label, null, properties);
	}

	/**
	 * 返回元素的属性-值，在添加属性是使用
	 * 
	 * @param key   键
	 * @param value 值
	 * @return 返回元组
	 */
	public static Pair prop(String key, String value) {
		return new Pair(key, value);
	}

//    封装元组
	public static class Pair {
		private String key;
		private String value;

		protected Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}
}
