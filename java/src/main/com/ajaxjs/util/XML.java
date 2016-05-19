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
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ajaxjs.Constant;

/**
 * DOM 操控 XML http://yuancihang.iteye.com/blog/592678
 * 
 * @author yuancihang
 *
 */
public class XML {
	private Element e;

	private XML(Element element) {
		this.e = element;
	}

	public void print() {
		NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			System.out.println("节点名: " + node.getNodeName() + ", 节点值: " + node.getNodeValue() + ", 节点类型: " + node.getNodeType());
		}
	}

	public Element getDomElement() {
		return e;
	}

	/**
	 * 创建一份新的 XML，包含一个新的节点（根节点）
	 * 
	 * @param rootName
	 *            根节点名称
	 * @return
	 */
	public static XML newDom(String rootName) {
		Document doc = getDocBuilder().newDocument();
		doc.setXmlStandalone(true);

//		getDocBuilder();
		Element root = doc.createElement(rootName);
		doc.appendChild(root);
		
		return new XML(root);
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
	public static Element getRoot(String xmlFile) {
		try (InputStream is = new FileInputStream(xmlFile);) {
			return getRoot(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
 
	public List<Element> elements(String elementName) {
		List<Element> eList = new ArrayList<>();
		NodeList nodeList = e.getElementsByTagName(elementName);
		
		if (nodeList == null)
			return null;

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				eList.add(new XML(element));
			}
		}
		return eList;
	}

	/**
	 * 子节点是否有某个 tag
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
	 */
	public boolean hasChild(String name) {
		NodeList nodeList = e.getElementsByTagName(name);
		if ((nodeList == null) || (nodeList.getLength() < 1)) {
			return false;
		}
		return true;
	}

	/**
	 * 返回子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
	 */
	public Element child(String name) {
		return hasChild(name) ? (Element) e.getElementsByTagName(name).item(0) : null;
	}

	/**
	 * 设置节点文本值
	 * 
	 * @param value
	 *            节点文本值
	 * @return
	 */
	public void updateText(String value) {
		Node textNode = e.getFirstChild();
		textNode.setNodeValue(value);
	}

	/**
	 * 获取节点文本值
	 * @return
	 */
	public String getText() {
		Node textNode = e.getFirstChild();
		return textNode.getNodeValue();
	}

	/**
	 * 添加子节点
	 * 
	 * @param name
	 *            子节点 tag
	 * @return
	 */
	public Element addElement(String name) {
		Element element = e.getOwnerDocument().createElement(name);
		e.appendChild(element);

		return element;
	}

	/**
	 * 添加子节点，并设置文本
	 * 
	 * @param name
	 *            子节点 tag
	 * @param value
	 *            文本
	 * @return
	 */
	public Element addElement(String name, String value) {
		Document document = e.getOwnerDocument();
		Element child = addElement(name);
		child.appendChild(document.createTextNode(value));
		
		return child;
	}
 
	public void removeElement(String name) {
		NodeList nodeList = e.getElementsByTagName(name);
		if (nodeList == null) {
			return;
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			e.removeChild(nodeList.item(i));
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
			transformer.transform(new DOMSource(e.getOwnerDocument()), new StreamResult(new OutputStreamWriter(os)));
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
	public static String xpath(String xmlFile, String encoding, String expression, final Map<String, String> namespaceMap) {
		String value = null;
		InputSource source = null;

		try (InputStreamReader isr = new InputStreamReader(XML.class.getResourceAsStream(xmlFile), encoding);) {
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
