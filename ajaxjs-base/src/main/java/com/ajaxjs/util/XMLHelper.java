package com.ajaxjs.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * https://blog.csdn.net/axman/article/details/420910
 * 
 * @author Administrator
 *
 */
public class XMLHelper {
	/**
	 * 获取某个节点
	 * 
	 * @param dbCfg
	 * @param xpath
	 * @param fn
	 */
	public static void xPath(String dbCfg, String xpath, Consumer<Node> fn) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		try {
			XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xpath);
			NodeList nodes = (NodeList) expr.evaluate(factory.newDocumentBuilder().parse(dbCfg), XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++)
				fn.accept(nodes.item(i));

		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将节点所有属性都转换为 map
	 * 
	 * @param dbCfg
	 * @param xpath
	 * @return
	 */
	public static Map<String, String> nodeAsMap(String dbCfg, String xpath) {
		Map<String, String> map = new HashMap<>();
		
		XMLHelper.xPath(dbCfg, xpath, node -> {
			NamedNodeMap _map = node.getAttributes();
			
			 if (_map != null) {
			     for (int i = 0, n = _map.getLength(); i < n; i++) {
			    	 
			    	 Node _node = _map.item(i);
			    	 map.put( _node.getNodeName(), _node.getNodeValue());
			    	 
			     }
			  }
		});

		if(map.size() == 0)
			return null;
		
		return map;
	}

}
