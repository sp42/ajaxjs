package com.ajaxjs.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

import com.ajaxjs.Constant;

/**
 * DOM 操控 XML 可参考：http://yuancihang.iteye.com/blog/592678
 */
public class XML_Wrapper {
	private Element element;

	private XML_Wrapper(Element element) {
		this.element = element;
	}

	public Element getDomElement() {
		return element;
	}

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
//		child.getDomElement().appendChild(element.getOwnerDocument().createTextNode(value));

		return child;
	}

	/**
	 * 返回子节点列表
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
	 */
	public NodeList getChildren(String name) {
		NodeList nodeList = element.getElementsByTagName(name);
		if ((nodeList == null) || (nodeList.getLength() < 1)) {
			return null;
		}
		return nodeList;
	}

	/**
	 * 是否某个子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
	 */
	public boolean hasChild(String name) {
		return getChildren(name) != null;
	}

	/**
	 * 返回第一个子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
	 */
	public XML_Wrapper child(String name) {
		return getFirstChild(name);
	}

	/**
	 * 返回第一个子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
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
	 * @return
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
			System.out.println(
					"节点名: " + node.getNodeName() + ", 节点值: " + node.getNodeValue() + ", 节点类型: " + node.getNodeType());
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

	public void write(OutputStream os) {
		write(os, Constant.encoding_UTF8);
	}

	/**
	 * 
	 * @param os
	 * @param encoding
	 */
	public void write(OutputStream os, String encoding) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		tFactory.setAttribute("indent-number", 2);

		try {
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.transform(new DOMSource(element.getOwnerDocument()),
					new StreamResult(new OutputStreamWriter(os)));
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}
	}

	public void write(String xmlFile) {
		write(xmlFile, Constant.encoding_UTF8);
	}

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
	 * @return
	 */
	public static XML_Wrapper newDom(String rootName) {
		Document doc = getDocBuilder().newDocument();
		doc.setXmlStandalone(true);

		// getDocBuilder();
		Element root = doc.createElement(rootName);
		doc.appendChild(root);

		return new XML_Wrapper(root);
	}

	private static DocumentBuilder getDocBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 送入流创建 XML 文档
	 * 
	 * @param is
	 *            流，并不会自动关闭的
	 * @return
	 */
	public static Element getRoot(InputStream is) {
		Document doc = null;

		try {
			doc = getDocBuilder().parse(is);
			return doc.getDocumentElement();
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static XML_Wrapper getRoot(String xmlFile) {
		try (InputStream is = new FileInputStream(xmlFile);) {
			return new XML_Wrapper(getRoot(is));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 实用XPATH解析xml的工具 xpath表达式用法: /图书馆/书/@名称 , /configuration/property[name
	 * ='host']/value http://yuancihang.iteye.com/blog/592705
	 * 
	 * <?xml version="1.0"?>
	 * 
	 * <configuration>
	 * 
	 * <property> <name a="1">host</name> <value>http://192.168.3.249</value>
	 * <description>系统主机地址</description> </property>
	 * 
	 * <property> <name>login</name> <value>/page/Default.aspx</value>
	 * <description>登陆页面</description> </property>
	 * 
	 * </configuration> XpathTool xpath = new XpathTool("d:/test.xml");
	 * System.out.println(xpath.compute(
	 * "/configuration/property[name = 'host']/value", null));
	 * 
	 * @author yuan
	 * @param xmlFile
	 * @param encoding
	 * @param expression
	 * @param namespaceMap
	 * @return
	 */
	public static String xpath(String xmlFile, String encoding, String expression,
			final Map<String, String> namespaceMap) {
		String value = null;
		InputSource source = null;

		try (InputStreamReader isr = new InputStreamReader(XML_Wrapper.class.getResourceAsStream(xmlFile), encoding);) {
			source = new InputSource(isr);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			System.out.println("设置名称空间...");
		}

		try {
			value = xpath.evaluate(expression, source);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return value;
	}
}
