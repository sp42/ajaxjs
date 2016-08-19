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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DOM 操控 XML
 * 
 * @author frank
 */
public class XML_Wrapper {
	private Element element;

	/**
	 * 传入一个元素
	 * 
	 * @param element
	 *            元素
	 */
	private XML_Wrapper(Element element) {
		this.element = element;
	}

	/**
	 * 返回元素
	 * 
	 * @return 元素
	 */
	public Element getDomElement() {
		return element;
	}

	/**
	 * 设置属性
	 * 
	 * @param name
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public void setAttribute(String name, String value) {
		element.setAttribute(name, value);
	}

	/**
	 * 设置节点文本值
	 * 
	 * @param value
	 *            节点文本值
	 */
	public void text(String value) {
		Node textNode = element.getFirstChild();
		textNode.setNodeValue(value);
	}

	/**
	 * 获取节点文本值
	 * 
	 * @return 节点文本值
	 */
	public String text() {
		Node textNode = element.getFirstChild();
		return textNode.getNodeValue();
	}

	/**
	 * 添加子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return 子节点 XML_Wrapper
	 */
	public XML_Wrapper addElement(String name) {
		Element element = this.element.getOwnerDocument().createElement(name.trim());
		this.element.appendChild(element);

		return new XML_Wrapper(element);
	}

	/**
	 * 添加子节点，并设置文本
	 * 
	 * @param name
	 *            子节点 tag
	 * @param value
	 *            文本
	 * @return 子节点 XML_Wrapper
	 */
	public XML_Wrapper addElement(String name, String value) {
		XML_Wrapper child = addElement(name);
		child.getDomElement().appendChild(element.getOwnerDocument().createTextNode(value));

		return child;
	}

	/**
	 * 返回子节点列表
	 * 
	 * @param name
	 *            子节点 tag
	 * @return 子节点列表
	 */
	public NodeList getChildren(String name) {
		NodeList nodeList = element.getElementsByTagName(name);
		if ((nodeList == null) || (nodeList.getLength() < 1)) {
			return null;
		}
		return nodeList;
	}

	/**
	 * 是否拥有某个子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return true 表示有该子节点
	 */
	public boolean hasChild(String name) {
		return getChildren(name) != null;
	}

	/**
	 * 返回第一个子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return 第一个子节点，如果没有则返回 null
	 */
	public XML_Wrapper child(String name) {
		return getFirstChild(name);
	}

	/**
	 * 返回第一个子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return 第一个子节点
	 */
	public XML_Wrapper getFirstChild(String name) {
		if (hasChild(name)) {
			Node node = getChildren(name).item(0);
			return new XML_Wrapper((Element) node);
		}
		return null;
	}

	/**
	 * 获取子节点，忽略文本节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return 子节点
	 */
	public List<XML_Wrapper> getChildrenElement(String name) {
		NodeList nodeList = getChildren(name);
		if (nodeList == null)
			return null;

		List<XML_Wrapper> list = new ArrayList<>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				list.add(new XML_Wrapper(e));
			}
		}

		return list;
	}

	/**
	 * 打印子节点
	 */
	public void printChild() {
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			System.out.println("节点名: " + node.getNodeName() + ", 节点值: " + node.getNodeValue() + ", 节点类型: " + node.getNodeType());
		}
	}

	/**
	 * 移除子节点
	 * 
	 * @param name
	 *            子节点 tag
	 */
	public void removeChild(String name) {
		NodeList nodeList = getChildren(name);
		if (nodeList == null) {
			return;
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			element.removeChild(nodeList.item(i));
		}
	}

	/**
	 * 保存 XML 文件
	 * 
	 * @param os
	 *            输入流
	 */
	public void write(OutputStream os) {
		write(os, StandardCharsets.UTF_8.toString());
	}

	/**
	 * 保存 XML 文件
	 * 
	 * @param os
	 *            输入流
	 * @param encoding
	 *            编码
	 */
	public void write(OutputStream os, String encoding) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		tFactory.setAttribute("indent-number", 2);

		try {
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.transform(new DOMSource(element.getOwnerDocument()), new StreamResult(new OutputStreamWriter(os)));
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存 XML 文件
	 * 
	 * @param xmlFile
	 *            XML 文件保存路径
	 */
	public void write(String xmlFile) {
		write(xmlFile, StandardCharsets.UTF_8.toString());
	}

	/**
	 * 保存 XML 文件
	 * 
	 * @param xmlFile
	 *            XML 文件保存路径
	 * @param encoding
	 *            编码
	 */
	public void write(String xmlFile, String encoding) {
		try (OutputStream os = new FileOutputStream(xmlFile);) {
			write(os, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建一份新的 XML，包含一个新的节点（根节点）
	 * 
	 * @param rootName
	 *            根节点名称
	 * @return XML 实例
	 */
	public static XML_Wrapper newDom(String rootName) {
		Document doc = getDocBuilder().newDocument();
		doc.setXmlStandalone(true);

		// getDocBuilder();
		Element root = doc.createElement(rootName);
		doc.appendChild(root);

		return new XML_Wrapper(root);
	}

	/**
	 * 获取文档构建器
	 * @return 文档构建器
	 */
	private static DocumentBuilder getDocBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
 
	/**
	 * 读取 XML 文件
	 * 
	 * @param xmlFile
	 *            文件路径
	 * @return XML 实例
	 */
	public static XML_Wrapper getRoot(String xmlFile) {
		try (InputStream is = new FileInputStream(xmlFile);) {
			Document doc = getDocBuilder().parse(is);
			Element root = doc.getDocumentElement();
			
			return new XML_Wrapper(root);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 按照 XPath 查询
	 * 
	 * @param xmlFile
	 *            XML 文件路径
	 * @param encoding
	 *            XML 文件编码
	 * @param expression
	 *            XPath 表达式
	 * @param namespaceMap
	 *            命名空间
	 * @return XPath 的值
	 */
	public static String xpath(String xmlFile, String encoding, String expression, final Map<String, String> namespaceMap) {
		XPath xpath = XPathFactory.newInstance().newXPath();

		if (namespaceMap != null) {
			xpath.setNamespaceContext(new NamespaceContext() {
				@Override
				public String getNamespaceURI(String prefix) {
					if (prefix == null)
						throw new NullPointerException("Null prefix");
					else if ("xml".equals(prefix))
						return XMLConstants.XML_NS_URI;
					else if (namespaceMap.containsKey(prefix))
						return namespaceMap.get(prefix);

					return XMLConstants.NULL_NS_URI;
				}

				@Override
				public String getPrefix(String namespaceURI) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<?> getPrefixes(String namespaceURI) {
					throw new UnsupportedOperationException();
				}
			});
		}

		String value = null;

		try (FileInputStream is = new FileInputStream(xmlFile);
				InputStreamReader isr = new InputStreamReader(is, encoding);) {
			try {
				value = xpath.evaluate(expression, new InputSource(isr));
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;
	}
}
