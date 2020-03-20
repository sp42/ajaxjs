package com.ajaxjs.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ajaxjs.util.logger.LogHelper;

/**
 * https://blog.csdn.net/axman/article/details/420910
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class XMLHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(XMLHelper.class);

	/**
	 * 创建 DocumentBuilderFactory 实例
	 * 
	 * @return DocumentBuilderFactory 实例
	 */
	public static DocumentBuilderFactory initBuilderFactory() {
		return DocumentBuilderFactory.newInstance();
	}

	/**
	 * XML 转换需要的对象
	 * 
	 * @return XML 转换需要的对象
	 */
	public static DocumentBuilder initBuilder() {
		try {
			return initBuilderFactory().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 获取某个节点
	 * 
	 * @param xml   XML 文件路径
	 * @param xpath XPath 路径
	 * @param fn    处理节点的函数，传入 Node 类型节点
	 */
	public static void xPath(String xml, String xpath, Consumer<Node> fn) {
		DocumentBuilderFactory factory = initBuilderFactory();
		factory.setNamespaceAware(true);

		try {
			XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xpath);
			NodeList nodes = (NodeList) expr.evaluate(factory.newDocumentBuilder().parse(xml), XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++)
				fn.accept(nodes.item(i));
		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 解析 XML
	 * 
	 * @param xmlContent XML 内容
	 * @param fn         处理节点的函数，传入 Element 类型节点和 NodeList 类型子元素列表
	 */
	public static void parseXML(String xmlContent, BiConsumer<Element, NodeList> fn) {
		try (InputStream in = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"))) {
			Element el = initBuilder().parse(in).getDocumentElement();
			NodeList nodeList = el.getChildNodes();

			fn.accept(el, nodeList);
		} catch (SAXException | IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 将节点所有属性都转换为 map
	 * 
	 * @param xml   XML 文件路径
	 * @param xpath XPath 路径
	 * @return 节点 map
	 */
	public static Map<String, String> nodeAsMap(String xml, String xpath) {
		Map<String, String> map = new HashMap<>();

		XMLHelper.xPath(xml, xpath, node -> {
			NamedNodeMap _map = node.getAttributes();

			if (_map != null) {
				for (int i = 0, n = _map.getLength(); i < n; i++) {
					Node _node = _map.item(i);
					map.put(_node.getNodeName(), _node.getNodeValue());
				}
			}
		});

		if (map.size() == 0)
			return null;

		return map;
	}

}
