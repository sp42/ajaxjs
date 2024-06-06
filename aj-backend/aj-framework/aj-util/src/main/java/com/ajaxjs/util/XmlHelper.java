/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * XML 处理工具类
 * <a href="https://blog.csdn.net/axman/article/details/420910">...</a>
 *
 * @author sp42 frank@ajaxjs.com
 */
@Slf4j
public class XmlHelper {
    /**
     * XML 转换需要的对象
     *
     * @return XML 转换需要的对象
     */
    public static DocumentBuilder initBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.warn("WARN>>>>>", e);
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xpath);
            NodeList nodes = (NodeList) expr.evaluate(factory.newDocumentBuilder().parse(xml), XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++)
                fn.accept(nodes.item(i));
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            log.warn("WARN>>>>>", e);
        }
    }

    /**
     * 解析 XML
     *
     * @param xml XML 内容
     * @param fn  处理节点的函数，传入 Element 类型节点和 NodeList 类型子元素列表
     */
    public static void parseXML(String xml, BiConsumer<Node, NodeList> fn) {
        try (InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            Element el = Objects.requireNonNull(initBuilder()).parse(in).getDocumentElement();
            NodeList nodeList = el.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                fn.accept(node, nodeList);
            }
        } catch (SAXException | IOException e) {
            log.warn("WARN>>>>>", e);
        }
    }

    /**
     * 根据给定的 XML 字符串获取根元素
     *
     * @param xml XML字符串
     * @return 根元素
     */
    public static Element getRoot(String xml) {
        try (InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            return Objects.requireNonNull(initBuilder()).parse(in).getDocumentElement();
        } catch (SAXException | IOException e) {
            log.warn("WARN>>>>>", e);
        }

        return null;
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

        XmlHelper.xPath(xml, xpath, node -> {
            NamedNodeMap _map = node.getAttributes();

            if (_map != null) {
                for (int i = 0, n = _map.getLength(); i < n; i++) {
                    Node _node = _map.item(i);
                    map.put(_node.getNodeName(), _node.getNodeValue());
                }
            }
        });

        if (map.size() == 0) return null;

        return map;
    }

    /**
     * 获取节点内的文本内容，包括标签
     *
     * @param node 节点对象
     * @return innerBody
     */
    public static String getNodeText(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        DOMConfiguration domConfig = lsSerializer.getDomConfig();
        domConfig.setParameter("xml-declaration", false);
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < childNodes.getLength(); i++)
            sb.append(lsSerializer.writeToString(childNodes.item(i)));

        return sb.toString();
    }

    /**
     * 获取节点属性值
     *
     * @param node     节点对象
     * @param attrName 属性名称
     * @return 属性值，如果空返回 null
     */
    public static String getNodeAttribute(Node node, String attrName) {
        NamedNodeMap attrs = node.getAttributes();

        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            if (attr.getNodeName().equals(attrName)) return attr.getValue();
        }

        return null;
    }

    /**
     * 获取指定的 attribute 值
     *
     * @param el       节点
     * @param attrName 属性名
     * @return 属性值
     */
    public static String getAttribute(Node el, String attrName) {
        NamedNodeMap attributes = el.getAttributes();

        if (attributes != null && attributes.getLength() > 0) {
            Node namedItem = attributes.getNamedItem(attrName);

            if (namedItem != null) return namedItem.getNodeValue();
            else return null;// LOGGER.warning("找不到属性 " + attrName);

        } else return null;
    }
}
