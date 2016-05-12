package com.ajaxjs.javatools;

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

import com.ajaxjs.core.Constant;

/**
 * DOM 操控 XML
 * http://yuancihang.iteye.com/blog/592678
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

	public static XML newDom(String rootName) {
		DocumentBuilder dombuilder = getDocBuilder();
		Document doc = dombuilder.newDocument();
		doc.setXmlStandalone(true);
		
		getDocBuilder();
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

	public static XML getRoot(InputStream is){
		Document doc = null;
		DocumentBuilder dombuilder = getDocBuilder();
		
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		 
		Element root = doc.getDocumentElement();
		return new XML(root);
	}

	public static XML getRoot(String xmlFile) {
		try (InputStream is = new FileInputStream(xmlFile);){
			return getRoot(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getAttributeValue(String attributeName) {
		return e.getAttribute(attributeName);
	}

	public boolean existElement(String elementName) {
		NodeList nodeList = e.getElementsByTagName(elementName);
		if ((nodeList == null) || (nodeList.getLength() < 1)) {
			return false;
		}
		return true;
	}

	public String elementText(String elementName) {
		Element element = (Element) e.getElementsByTagName(elementName).item(0);
		Node textNode = element.getFirstChild();
		if (textNode == null) {
			return "";
		}
		return textNode.getNodeValue();
	}

	public XML element(String elementName) {
		NodeList nodeList = e.getElementsByTagName(elementName);
		if ((nodeList == null) || (nodeList.getLength() < 1))return null;
		
		Element element = (Element) nodeList.item(0);
		return new XML(element);
	}

	public List<XML> elements(String elementName) {
		List<XML> eList = new ArrayList<>();
		NodeList nodeList = e.getElementsByTagName(elementName);
		if (nodeList == null)return eList;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				eList.add(new XML(element));
			}
		}
		return eList;
	}

	public XML addElement(String name) {
		Document document = e.getOwnerDocument();
		Element element = document.createElement(name);
		e.appendChild(element);
		
		return new XML(element);
	}

	public XML addElement(String name, String value) {
		Document document = e.getOwnerDocument();
		Element element = document.createElement(name);
		e.appendChild(element);
		Text text = document.createTextNode(value);
		element.appendChild(text);
		
		return new XML(element);
	}

	// 添加或修改属性
	public XML setAttribute(String name, String value) {
		e.setAttribute(name, value);
		return this;
	}

	public void remove(XML subDom) {
		e.removeChild(subDom.getDomElement());
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

	public void removeAttribute(String name) {
		e.removeAttribute(name);
	}

	public XML updateElementText(String name, String value) {
		Element element = (Element) e.getElementsByTagName(name).item(0);
		Node textNode = element.getFirstChild();
		textNode.setNodeValue(value);
		return new XML(element);
	}

	public XML updateElementText(String value) {
		Node textNode = e.getFirstChild();
		textNode.setNodeValue(value);
		return this;
	}

	public String getElementText() {
		Node textNode = e.getFirstChild();
		return textNode.getNodeValue();
	}

	public void write(OutputStream os) {
		write(os, Constant.encoding_UTF8);
	}

	public void write(OutputStream os, String encoding) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			tFactory.setAttribute("indent-number", 2);
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
		try (OutputStream os = new FileOutputStream(xmlFile);){
			write(os, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 实用XPATH解析xml的工具 xpath表达式用法: /图书馆/书/@名称 , /configuration/property[name ='host']/value
	 * http://yuancihang.iteye.com/blog/592705
	 * 
	 * <?xml version="1.0"?>
	
	<configuration>
	
	<property>
	  <name a="1">host</name>
	  <value>http://192.168.3.249</value>
	  <description>系统主机地址</description>
	</property>
	
	<property>
	  <name>login</name>
	  <value>/page/Default.aspx</value>
	  <description>登陆页面</description>
	</property>
	
	</configuration>
	 * XpathTool xpath = new XpathTool("d:/test.xml");
	   System.out.println(xpath.compute("/configuration/property[name = 'host']/value", null));
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

		try(InputStreamReader isr = new InputStreamReader(XML.class.getResourceAsStream(xmlFile), encoding);) {
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
