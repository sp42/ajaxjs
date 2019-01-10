package com.ajaxjs.keyvalue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ajaxjs.js.JsonHelper;

/**
 * 为 key-value 结构的数据提供数据转换的中间件
 * 
 * @author Frank Cheung
 *
 */
public class MappingHelper {

	private Map<String, Object> map;

	private Object bean;

	private String json;

	private boolean addJsonPerfix;

	public MappingHelper(Map<String, Object> map) {
		this.map = map;
	}

	public MappingHelper(Object bean) {
		this.bean = bean;
	}

	public MappingHelper(String json) {
		this.json = json;
	}

	public MappingHelper() {
	}

	/**
	 * 添加控制器能识别 “json::”
	 * 
	 * @return 当前实例以便链式调用
	 */
	public MappingHelper addJsonPerfix() {
		addJsonPerfix = true;
		return this;
	}

	/**
	 * 
	 * @param obj
	 * @return 当前实例以便链式调用
	 */
	public MappingHelper setObject(Object obj) {
		this.bean = obj;
		return this;
	}

	/**
	 * 转换为 JSON 字符串输出
	 * 
	 * @return JSON 字符串
	 */
	public String toJson() {
		if (map != null) {
			json = MappingJson.stringifyMap(map);
		} else if (bean != null) {
			json = BeanUtil.beanToJson(bean);
		} else if (bean != null) {
			json = MappingJson.stringifySimpleObject(bean);
		}

		if (json != null && addJsonPerfix)
			return "json::" + json;

		return json;
	}

	public Map<String, Object> toMap() {
		if (json != null) {
			return JsonHelper.parseMap(json);
		}

		return null;
	}

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "json::{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * 操作成功，返回 msg 信息，可扩展字段的
	 */
	public static final String json_ok_extension = "json::{\"isOk\": true, \"msg\" : \"%s\", %s}";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "json::{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 输出 JSON OK
	 * 
	 * @param msg
	 * @return
	 */
	public static String jsonOk(String msg) {
		return String.format(json_ok, msg);
	}

	/**
	 * 输出 JSON No OK
	 * 
	 * @param msg
	 * @return
	 */
	public static String jsonNoOk(String msg) {
		return String.format(json_not_ok, msg);
	}


	/**
	 * 把 Map 集合转换为 JSON 数组
	 * 
	 * @param result Map 集合
	 * @return JSON 结果
	 */
	public static String outputListMapAsJson(List<Map<String, Object>> result) {
		if (result != null && result.size() > 0)
			return "json::{\"result\":" + MappingJson.stringifyListMap(result) + "}";
		else
			return "json::{\"result\": null}";
	}

	/**
	 * 把 Bean 集合转换为 JSON 数组
	 * 
	 * @param result BaseMolde 集合
	 * @return JSON 结果
	 */
	public static String outputListBeanAsJson(List<?> result) {
		if (result != null && result.size() > 0) {
			String[] str = new String[result.size()];

			for (int i = 0; i < result.size(); i++)
				str[i] = BeanUtil.beanToJson(result.get(i));

			return "json::{\"result\":[" + String.join(",", str) + "]}";
		} else
			return "json::{\"result\": null}";
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Object getObj() {
		return bean;
	}

	public void setObj(Object obj) {
		this.bean = obj;
	}

	public String getJson() {
		return json;
	}

	public MappingHelper setJson(String json) {
		this.json = json;
		return this;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFromJson(String json, String key, Class<T> clz) {
		Map<String, Object> map = new MappingHelper().setJson(json).toMap();

		if (map != null && map.get(key) != null) {
			return (T) map.get(key);
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
			e.printStackTrace();
			return null;
		}
	}

	static DocumentBuilder initBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
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
	public static String mapToXml(Map<String, String> data) {
		Document doc = initBuilder().newDocument();
		Element root = doc.createElement("xml");
		doc.appendChild(root);

		for (String key : data.keySet()) {
			String value = data.get(key);
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
		} catch (IOException | TransformerException | TransformerFactoryConfigurationError e1) {
			e1.printStackTrace();
		}

		return null;
	}
}
