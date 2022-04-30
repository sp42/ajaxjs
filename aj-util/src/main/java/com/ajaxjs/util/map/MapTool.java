/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.map;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.XmlHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * Map 转换工具
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MapTool {
	private static final LogHelper LOGGER = LogHelper.getLog(MapTool.class);

	// --------------------------------------------------------------------------------------------------
	// -----------------------------------------------Map转换---------------------------------------------
	// --------------------------------------------------------------------------------------------------

	/**
	 * Map 转换为 String
	 * 
	 * @param map Map 结构，Key 必须为 String 类型
	 * @param div 分隔符
	 * @param fn  对 Value 的处理函数，返回类型 T
	 * @return Map 序列化字符串
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
		return join(map, "&");
	}

	/**
	 * String[] 转换为 Map
	 * 
	 * @param pairs 结对的字符串数组，包含 = 字符分隔 key 和 value
	 * @param fn    对 Value 的处理函数，返回类型 Object
	 * @return Map 对象
	 */
	public static Map<String, Object> toMap(String[] pairs, Function<String, Object> fn) {
		if (ObjectUtils.isEmpty(pairs))
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
	 * String[] 转换为 Map，key 与 value 分别一个数组
	 * 
	 * @param columns 结对的键数组
	 * @param values  结对的值数组
	 * @param fn      对 Value 的处理函数，返回类型 Object
	 * @return Map 对象
	 */
	public static Map<String, Object> toMap(String[] columns, String[] values, Function<String, Object> fn) {
		if (ObjectUtils.isEmpty(columns))
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
	 * @param map 输入的map
	 * @param key map的键
	 * @param s   如果过非空，那么接着要做什么？在这个回调函数中处理。传入的参数就是map.get(key)的值
	 */
	public static <T> void getValue(Map<String, T> map, String key, Consumer<T> s) {
		if (map != null) {
			T value = map.get(key);
			if (value != null)
				s.accept(value);
		}
	}

	/**
	 * 万能 Map 转换器，为了泛型的转换而设的一个方法，怎么转换在 fn 中处理
	 * 
	 * @param map 原始 Map，key 必须为 String 类型
	 * @param fn  转换函数
	 * @return 转换后的 map
	 */
	public static <T, K> Map<String, T> as(Map<String, K> map, Function<K, T> fn) {
		Map<String, T> _map = new HashMap<>();
		map.forEach((k, v) -> _map.put(k, v == null ? null : fn.apply(v)));

		return _map;
	}

	public static Map<String, Object> as(Map<String, String[]> map) {
		return as(map, arr -> MappingValue.toJavaValue(arr[0]));
	}

	// --------------------------------------------------------------------------------------------------
	// -----------------------------------------------Bean-----------------------------------------------
	// --------------------------------------------------------------------------------------------------

	@FunctionalInterface
	public static interface EachFieldArg {
		public void item(String key, Object value, PropertyDescriptor property);
	}

	/**
	 * 遍历一个 Java Bean
	 * 
	 * @param bean Java Bean
	 * @param fn   执行的任务，参数有 key, value, property
	 */
	public static void eachField(Object bean, EachFieldArg fn) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String key = property.getName();

				// 得到 property 对应的 getter 方法
				Method getter = property.getReadMethod();
				Object value = getter.invoke(bean);

				fn.item(key, value, property);
			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * Map 转为 Bean
	 * 
	 * @param map         原始数据
	 * @param clz         实体 bean 的类
	 * @param isTransform 是否尝试转换值
	 * @param isChild     是否处理子对象
	 * @return 实体 bean 对象
	 */
	public static <T> T map2Bean(Map<String, ?> map, Class<T> clz, boolean isTransform, boolean isChild) {
		T bean = ReflectUtil.newInstance(clz);

		eachField(bean, (key, v, property) -> {
			try {
				if (map.containsKey(key)) {
					Object value = map.get(key);

					// null 是不会传入 bean 的
					if (value != null) {
						Class<?> t = property.getPropertyType(); // Bean 值的类型，这是期望传入的类型，也就 setter 参数的类型

						if (isTransform && t != value.getClass()) // 类型相同，直接传入；类型不相同，开始转换
							value = MappingValue.objectCast(value, t);

//						LOGGER.info("t:" + t);
//						LOGGER.info("v:" + value + " type: " + value.getClass());

						try {
							property.getWriteMethod().invoke(bean, value);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							LOGGER.info("method:" + property.getWriteMethod());
							LOGGER.info("value type:" + value.getClass() + " vlaue: " + value);
						}
					}
				}

				// 子对象
				if (isChild) {
					for (String mKey : map.keySet()) {
						if (mKey.contains(key + '_')) {
							Method getter = property.getReadMethod(), setter = property.getWriteMethod();// 得到对应的 setter 方法

							Object subBean = getter.invoke(bean);
							String subBeanKey = mKey.replaceAll(key + '_', "");

							if (subBean != null) {// 已有子 bean
								if (map.get(mKey) != null) // null 值不用处理
									ReflectUtil.setProperty(subBean, subBeanKey, map.get(mKey));
							} else { // map2bean
								Map<String, Object> subMap = new HashMap<>();
								subMap.put(subBeanKey, map.get(mKey));
								subBean = map2Bean(subMap, setter.getParameterTypes()[0], isTransform);
								setter.invoke(bean, subBean); // 保存新建的 bean
							}
						}
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e instanceof IllegalArgumentException)
					LOGGER.warning("[{0}] 参数类型不匹配，输入值是 [{1}]", key, v);

				LOGGER.warning(e);
			}
		});

		return bean;
	}

	public static <T> T map2Bean(Map<String, ?> map, Class<T> clz, boolean isTransform) {
		return map2Bean(map, clz, isTransform, true);
	}

	/**
	 * map 转实体
	 * 
	 * @param map 原始数据
	 * @param clz 实体 bean 的类
	 * @return 实体 bean 对象
	 */
	public static <T> T map2Bean(Map<String, ?> map, Class<T> clz) {
		return map2Bean(map, clz, false);
	}

	/**
	 * Bean 转为 Map
	 * 
	 * @param bean 实体 bean 对象
	 * @return Map 对象
	 */
	public static <T> Map<String, Object> bean2Map(T bean) {
		Map<String, Object> map = new HashMap<>();

		eachField(bean, (k, v, property) -> {
			if (v != null && !k.equals("class")) // 过滤 class 属性
				map.put(k, v);
		});

		return map;
	}

	// --------------------------------------------------------------------------------------------------
	// -----------------------------------------------XML------------------------------------------------
	// --------------------------------------------------------------------------------------------------

	public static String beanToXml(Object bean) {
		return mapToXml(bean2Map(bean));
	}

	/**
	 *
	 * 将 Map 转换为 XML 格式的字符串
	 *
	 * @param data Map 类型数据
	 * @return XML 格式的字符串
	 */
	public static String mapToXml(Map<String, ?> data) {
		Document doc = XmlHelper.initBuilder().newDocument();
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
	 * XML 格式字符串转换为 Map
	 *
	 * @param strXML XML 字符串
	 * @return XML 数据转换后的 Map
	 */
	public static Map<String, String> xmlToMap(String strXML) {
		if (strXML == null)
			return null;

		Map<String, String> data = new HashMap<>();

		try (InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));) {
			Document doc = XmlHelper.initBuilder().parse(stream);
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
