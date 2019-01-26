package com.ajaxjs.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ajaxjs.util.logger.LogHelper;

public class MapTool {
	private static final LogHelper LOGGER = LogHelper.getLog(MapTool.class);

	/**
	 * Map 转换为 String
	 * 
	 * @param map Map
	 * @return String
	 */
	public static <T> String join(Map<String, T> map, String div, Function<T, String> fn) {
		String[] pairs = new String[map.size()];

		int i = 0;

		for (String key : map.keySet())
			pairs[i++] = key + "=" + fn.apply(map.get(key));

		return String.join(div, pairs);
	}

	public static <T> String join(Map<String, T> map, Function<T, String> fn) {
		return join(map, "&", fn);
	}

	public static <T> String join(Map<String, T> map, String div) {
		return join(map, div, v -> v == null ? null : v.toString());
	}

	public static <T> String join(Map<String, T> map) {
		return join(map, v -> v == null ? null : v.toString());
	}

	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param pairs 结对的字符串数组
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] pairs, Function<String, Object> fn) {
		if (CommonUtil.isNull(pairs))
			return null;

		Map<String, Object> map = new HashMap<>();

		for (String pair : pairs) {
			if (!pair.contains("="))
				throw new IllegalArgumentException("没有 = 不能转化为 map");

			String[] column = pair.split("=");

			if (column.length >= 2)
				map.put(column[0], fn == null ? column[1] : fn.apply(column[1]));
			else
				map.put(column[0], "");// 没有 等号后面的，那设为空字符串
		}

		return map;
	}

	/**
	 * 数据结构的简单转换 String[]-->Map
	 * 
	 * @param columns 结对的键数组
	 * @param values 结对的值数组
	 * @return Map 结构
	 */
	public static Map<String, Object> toMap(String[] columns, String[] values, Function<String, Object> fn) {
		if (CommonUtil.isNull(columns))
			return null;

		if (columns.length != values.length)
			throw new UnsupportedOperationException("两个数组 size 不一样");

		Map<String, Object> map = new HashMap<>();

		int i = 0;
		for (String column : columns)
			map.put(column, fn.apply(values[i++]));

		return map;
	}

	/**
	 * 判断 map 非空，然后根据 key 获取 value，若 value 非空则作为参数传入函数接口 s
	 * 
	 * @param map
	 * @param key
	 * @param s
	 */
	public static <T> void getValue(Map<String, T> map, String key, Consumer<T> s) {
		if (map != null) {
			T value = map.get(key);
			if (value != null)
				s.accept(value);
		}
	}

	public static <T, K> Map<String, T> as(Map<String, K> map, Function<K, T> fn) {
		Map<String, T> _map = new HashMap<>();

		for (String key : map.keySet()) {
			K value = map.get(key);
			_map.put(key.toString(), value == null ? null : fn.apply(value));
		}

		return _map;
	}

	public static DocumentBuilder initBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 *
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data Map类型数据
	 * @return XML格式的字符串
	 */
	public static String mapToXml(Map<String, ?> data) {
		Document doc = initBuilder().newDocument();
		Element root = doc.createElement("xml");
		doc.appendChild(root);

		for (String key : data.keySet()) {
			System.out.println(key);
			String value = data.get(key).toString();
			if (value == null)
				value = "";

			Element filed = doc.createElement(key);
			filed.appendChild(doc.createTextNode(value.trim()));
			root.appendChild(filed);
		}

		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			try (StringWriter writer = new StringWriter();) {
				transformer.transform(new DOMSource(doc), new StreamResult(writer));
				String output = writer.getBuffer().toString(); // .replaceAll("\n|\r", "");
				return output;
			}
		} catch (IOException | TransformerException | TransformerFactoryConfigurationError e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 *
	 * 
	 * XML格式字符串转换为Map
	 *
	 * @param strXML XML字符串
	 * @return XML数据转换后的Map
	 */
	public static Map<String, String> xmlToMap(String strXML) {
		if (strXML == null)
			return null;

		Map<String, String> data = new HashMap<>();

		try (InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));) {
			Document doc = initBuilder().parse(stream);
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
