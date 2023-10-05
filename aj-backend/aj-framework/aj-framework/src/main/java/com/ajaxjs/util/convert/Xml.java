package com.ajaxjs.util.convert;

import com.ajaxjs.util.XmlHelper;
import com.ajaxjs.util.logger.LogHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Xml {
    public static final LogHelper LOGGER = LogHelper.getLog(Xml.class);

    public static String beanToXml(Object bean) {
        return mapToXml(Convert.bean2Map(bean));
    }

    /**
     * 将 Map 转换为 XML 格式的字符串
     *
     * @param data Map 类型数据
     * @return XML 格式的字符串
     */
    public static String mapToXml(Map<String, ?> data) {
        Document doc = Objects.requireNonNull(XmlHelper.initBuilder()).newDocument();
        Element root = doc.createElement("xml");
        doc.appendChild(root);

        data.forEach((k, v) -> {
            String value = data.get(k).toString();
            if (value == null)
                value = "";

            Element filed = doc.createElement(k);
            filed.appendChild(doc.createTextNode(value.trim()));
            root.appendChild(filed);
        });

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            try (StringWriter writer = new StringWriter()) {
                transformer.transform(new DOMSource(doc), new StreamResult(writer));

                return writer.getBuffer().toString();
            }
        } catch (IOException | TransformerException | TransformerFactoryConfigurationError e) {
            LOGGER.warning(e);
        }

        return null;
    }

    /**
     * XML 格式字符串转换为 Map
     *
     * @param strXML XML 字符串
     * @return XML 数据转换后的 Map
     */
    public static Map<String, String> xmlToMap(String strXML) {
        if (strXML == null)
            return null;

        Map<String, String> data = new HashMap<>();

        try (InputStream stream = new ByteArrayInputStream(strXML.getBytes(StandardCharsets.UTF_8))) {
            Document doc = Objects.requireNonNull(XmlHelper.initBuilder()).parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }

            return data;
        } catch (IOException | SAXException e) {
            LOGGER.warning(e);
            return null;
        }
    }
}
