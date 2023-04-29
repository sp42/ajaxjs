package com.ajaxjs.payment.alipay;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Frank Cheung
 */
public class OneLevelOnlyXML {
    private Document doc;

    private Element root;

    /**
     * 创建根元素
     *
     * @param tag
     */
    public void createRootElement(String tag) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.newDocument();
            root = doc.createElement(tag);
            doc.appendChild(root);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建子元素
     *
     * @param tag
     * @param value
     */
    public void createChild(String tag, String value) {
        if (doc == null || root == null)
            return;

        Element encrypt = doc.createElement(tag);
        encrypt.appendChild(doc.createTextNode(value));
        root.appendChild(encrypt);
    }

    public void createChild(String tag, int value) {
        createChild(tag, String.valueOf(value));
    }

    /**
     * 序列化为字符串
     *
     * @return XML 字符串
     */
    public String toXMLString() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
                transformer.transform(new DOMSource(doc), new StreamResult(out));

                return out.toString("utf-8");
            }
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
