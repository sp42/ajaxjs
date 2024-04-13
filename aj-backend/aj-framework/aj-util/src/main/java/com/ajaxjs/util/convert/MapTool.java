/**
 * Copyright sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.convert;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.XmlHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Map 转换工具
 *
 * @author sp42 frank@ajaxjs.com
 */
@Slf4j
public class MapTool {
    /**
     * Map 转换为 String
     *
     * @param map Map 结构，Key 必须为 String 类型
     * @param div 分隔符
     * @param fn  对 Value 的处理函数，返回类型 T
     * @param <T> Key 的类型
     * @return Map 序列化字符串
     */
    public static <T> String join(Map<String, T> map, String div, Function<T, String> fn) {
        String[] pairs = new String[map.size()];
        int i = 0;

        for (String key : map.keySet())
            pairs[i++] = key + "=" + fn.apply(map.get(key));

        return String.join(div, pairs);
    }

    /**
     * 将指定的 Map 对象转换为字符串，使用指定的分隔符分隔每个元素。
     *
     * @param map 要转换的 Map 对象
     * @param fn  将 Map 中的值转换为字符串的函数
     * @param <T> Map 中的值的类型
     * @return 转换后的字符串
     */
    public static <T> String join(Map<String, T> map, Function<T, String> fn) {
        return join(map, "&", fn);
    }

    /**
     * 将 Map 中的值使用指定的分隔符进行拼接。
     *
     * @param map 要拼接的 Map 对象
     * @param div 分隔符
     * @param <T> Map 中元素的类型
     * @return 拼接后的字符串
     */
    public static <T> String join(Map<String, T> map, String div) {
        return join(map, div, v -> v == null ? null : v.toString());
    }

    /**
     * 将给定的 Map 对象转换为字符串，使用指定的分隔符将键值对连接起来
     *
     * @param map 要转换的 Map 对象
     * @param <T> 键值对的类型
     * @return 连接后的字符串
     */
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
     * 判断 Map 非空，然后根据 key 获取 value，若 value 非空则作为参数传入函数接口
     *
     * @param map 输入的 Map
     * @param key map的键
     * @param s   如果过非空，那么接着要做什么？在这个回调函数中处理。传入的参数就是 map.get(key)的值
     * @param <T> 返回 value 的类型
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
     * @param <K> Key 的类型
     * @param <T> 返回 value 的类型
     * @return 转换后的 map
     */
    public static <T, K> Map<String, T> as(Map<String, K> map, Function<K, T> fn) {
        Map<String, T> _map = new HashMap<>();
        map.forEach((k, v) -> _map.put(k, v == null ? null : fn.apply(v)));

        return _map;
    }

    /**
     * 将给定的 map 转换为 Map&lt;String, Object&gt; 类型的结果
     *
     * @param map 要转换的 map，包含 String 和 String[] 类型的键值对
     * @return 转换后的 Map&lt;String, Object&gt; 类型的结果
     */
    public static Map<String, Object> as(Map<String, String[]> map) {
        return as(map, arr -> ConvertBasicValue.toJavaValue(arr[0]));
    }

    /**
     * 克隆一个 Map 到新的 Map 中， Map 深复制操作
     *
     * @param map 需要克隆的映射表
     * @param <T> 键的类型
     * @param <K> 值的类型
     * @return 克隆后的映射表
     */
    public static <T, K> Map<T, K> deepCopy(Map<T, K> map) {
        Map<T, K> newMap = new HashMap<>();

        for (T key : map.keySet())
            newMap.put(key, map.get(key));

        return newMap;
    }

    /**
     * 将给定的对象转换为 XML 格式的字符串
     *
     * @param bean 要转换的对象
     * @return 转换后的XML格式的字符串
     */
    public static String beanToXml(Object bean) {
        return mapToXml(EntityConvert.bean2Map(bean));
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
            transformer.setOutputProperty(OutputKeys.ENCODING, StrUtil.UTF8_SYMBOL);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            try (StringWriter writer = new StringWriter()) {
                transformer.transform(new DOMSource(doc), new StreamResult(writer));

                return writer.getBuffer().toString();
            }
        } catch (IOException | TransformerException | TransformerFactoryConfigurationError e) {
            log.warn("ERROR>>", e);
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
            log.warn("ERROR>>", e);
            return null;
        }
    }
}
