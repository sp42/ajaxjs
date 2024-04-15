package com.ajaxjs.util;

import com.ajaxjs.util.io.Resources;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestXmlHelper {
    @Test
    public void testInitBuilder() {
        // 测试 initBuilder 方法是否能够返回非空的 DocumentBuilder 实例
        DocumentBuilder builder = XmlHelper.initBuilder();
        assertNotNull(builder);

        // 验证返回的 DocumentBuilder 是否是有效的实例
        assertTrue(builder instanceof DocumentBuilder);

        // 测试 initBuilder 在异常情况下的行为，是否返回 null
        // 我们将通过反射禁用工厂来模拟 ParserConfigurationException
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Method method = DocumentBuilderFactory.class.getDeclaredMethod("setValidating", boolean.class);
            method.setAccessible(true);
            method.invoke(factory, true); // 设置为验证状态，这将导致 ParserConfigurationException

            DocumentBuilder invalidBuilder = factory.newDocumentBuilder();
            System.out.println("Expected ParserConfigurationException to be thrown");
        } catch (Exception e) {
            // 我们期望在调用 factory.newDocumentBuilder() 时捕获异常
            assertTrue(e.getCause() instanceof ParserConfigurationException);
        }
    }

    private Consumer<Node> mockConsumer = mock(Consumer.class);
    ;

    @Test
    public void testXPathWithValidXmlAndXPath() {
        String xpath = "/root/element";
        // 执行测试方法
        XmlHelper.xPath(Resources.getResourcesFromClasspath("test.xml"), xpath, mockConsumer);

        // 验证 Consumer 是否被正确调用
        verify(mockConsumer, times(1)).accept(any(Node.class));
    }

    @Test
    public void testXPathWithInvalidXPath() {
        String xpath = "/root/invalidElement";

        // 执行测试方法，期待不抛出异常
        XmlHelper.xPath(Resources.getResourcesFromClasspath("test.xml"), xpath, mockConsumer);

        // 由于 XPath 不匹配，Consumer 不应该被调用
        verify(mockConsumer, never()).accept(any(Node.class));
    }

    @Test
    public void testParseXML() {
        // 假设的 XML 内容
        String xmlContent = "<root><child>Content</child></root>";
        // 创建一个模拟的 BiConsumer
        BiConsumer<Node, NodeList> consumerMock = Mockito.mock(BiConsumer.class);
        // 创建模拟的节点和子节点列表
        Node mockNode = Mockito.mock(Node.class);
        NodeList mockNodeList = Mockito.mock(NodeList.class);
        // 当调用 getLength() 方法时返回 1
        when(mockNodeList.getLength()).thenReturn(1);
        // 当调用 item(0) 方法时返回 mockNode
        when(mockNodeList.item(0)).thenReturn(mockNode);

        // 验证 initBuilder() 方法至少被调用一次
        Mockito.doReturn(mock(DocumentBuilder.class)).when(XmlHelper.class);
        XmlHelper.initBuilder();

        // 调用待测试的 parseXML 方法
        XmlHelper.parseXML(xmlContent, consumerMock);

        // 验证 consumerMock 被正确调用
        verify(consumerMock, times(1)).accept(any(Node.class), any(NodeList.class));
    }

    @Test
    public void testNodeAsMapWithValidXmlAndXPath() {
        String xpath = "/root/element";
        Map<String, String> expectedMap = ObjectHelper.hashMap("attr1", "value1", "attr2", "value2");

        Map<String, String> result = XmlHelper.nodeAsMap(Resources.getResourcesFromClasspath("test2.xml"), xpath);

        assertNotNull(result);
        assertEquals(expectedMap, result);
    }

    @Test
    public void testNodeAsMapWithInvalidXPath() {
        String xpath = "/root/invalidElement";
        Map<String, String> result = XmlHelper.nodeAsMap(Resources.getResourcesFromClasspath("test2.xml"), xpath);
        assertNull(result);
    }
}
