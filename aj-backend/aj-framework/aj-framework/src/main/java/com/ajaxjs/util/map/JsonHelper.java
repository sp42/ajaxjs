/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.util.map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.jsonparser.JsonParseException;
import com.ajaxjs.jsonparser.syntax.FMS;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.logger.LogHelper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * 序列化/反序列化 JSON
 *
 * @author sp42 frank@ajaxjs.com
 */
public class JsonHelper {
    public static final LogHelper LOGGER = LogHelper.getLog(JsonHelper.class);

    /**
     * 解析 JSON 为 Map 或 List
     *
     * @param str JSON 字符串
     * @return Map 或 List
     */
    public static Object parse(String str) {
        str = removeCr(str);

        try {
            return new FMS(str).parse();
        } catch (JsonParseException e) {
            LOGGER.warning("JSON 解析错误：" + str);

            throw e;
        }
    }

    /**
     * 解析 JSON 字符串为 Map
     *
     * @param str JSON 字符串
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseMap(String str) {
        return (Map<String, Object>) parse(str);
    }

    /**
     * 解析 JSON 字符串为 List
     *
     * @param str JSON 字符串
     * @return List
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> parseList(String str) {
        return (List<Map<String, Object>>) parse(str);
    }

    /**
     * 解析 JSON 字符串为 List（Bean）
     *
     * @param json JSON 字符串
     * @param clz  Bean 类
     * @param <T>  Bean 类
     * @return List
     */
    public static <T> List<T> parseBeanList(String json, Class<T> clz) {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> maps = parseList(json);

        for (Map<String, Object> map : maps) {
            T bean = MapTool.map2Bean(map, clz);
            list.add(bean);
        }

        return list;
    }

    /**
     * 解析 JSON 字符串为 Map。清除 \r\n 字符
     *
     * @param str JSON 字符串
     * @return Map
     */
    public static Map<String, Object> parseMapClean(String str) {
        str = removeCr(str);
        return parseMap(str);
    }

    /**
     * 解析 JSON 字符串转换为 Bean 对象
     *
     * @param json JSON 字符串
     * @param clz  Bean 对象类引用
     * @return Bean 对象
     */
    public static <T> T parseMapAsBean(String json, Class<T> clz) {
        Map<String, Object> map = parseMap(json);
        return MapTool.map2Bean(map, clz, true); // 应该为 false
    }

    /**
     * 对 Object 尝试类型检测，将其合适地转换为 JSON 字符串返回。
     * <p>
     * FIXME: Bean 列表 还不行；Mysql 新版驱动返回日期 LocalDate 会死循环
     *
     * @param obj 任意对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof Boolean || obj instanceof Number) {
            if (obj instanceof Long) {
                Long l = (Long) obj;// js number 最大长度为 16，转换为字符串

                if (l > 100000L) {
                    String s = l.toString();
                    if (s.length() > 15) // gson 最大 15
                        return '\"' + s + '\"';
                }
            }

            return obj.toString();
        } else if (obj instanceof String)
            return '\"' + jsonString_covert((String) obj) + '\"';
        else if (obj instanceof String[])
            return jsonArr((String[]) obj, v -> "\"" + jsonString_covert(v) + "\"");
        else if (obj.getClass() == Integer[].class)
            return jsonArr((Integer[]) obj, String::valueOf);
        else if (obj.getClass() == int[].class) {
            Integer[] arr = Arrays.stream((int[]) obj).boxed().toArray(Integer[]::new);
            return jsonArr(arr, String::valueOf);
        } else if (obj instanceof Long[])
            return jsonArr((Long[]) obj, Object::toString);
        else if (obj instanceof long[]) {
            Long[] arr = Arrays.stream((long[]) obj).boxed().toArray(Long[]::new);
            return jsonArr(arr, Object::toString);
        } else if (obj instanceof Date)
            return '\"' + DateUtil.simpleDateFormatFactory(DateUtil.DATE_FORMAT).format((Date) obj) + '\"';
        else if (obj instanceof Map)
            return stringifyMap((Map<?, ?>) obj);
        else if (obj instanceof Map[])
            return jsonArr((Map<?, ?>[]) obj, JsonHelper::stringifyMap);
//		else if (obj instanceof ValueEnmu) {
//			if (obj instanceof IntegerValueEnum && ((IntegerValueEnum) obj).isUsingOrdinal())
//				return (((IntegerValueEnum) obj).getValue() + 1) + "";
//			else
//				return ((ValueEnmu) obj).getValue().toString();
//		} 
        else if (obj instanceof BaseModel || obj instanceof IBaseModel)
            return beanToJson(obj);
        else if (obj instanceof BaseModel[])
            return jsonArr((BaseModel[]) obj, JsonHelper::beanToJson);
        else if (obj instanceof IBaseModel[])
            return jsonArr((IBaseModel[]) obj, JsonHelper::beanToJson);
        else if (obj instanceof List) {
            List<?> list = (List<?>) obj;

            if (list.size() > 0) {
                if (list.get(0) instanceof Integer)
                    return toJson(list.toArray(new Integer[list.size()]));
                if (list.get(0) instanceof Long)
                    return toJson(list.toArray(new Long[list.size()]));
                else if (list.get(0) instanceof String)
                    return toJson(list.toArray(new String[list.size()]));
                else if (list.get(0) instanceof Map) // Map 类型的输出
                    return toJson(list.toArray(new Map[list.size()]));
                else if (list.get(0) instanceof BaseModel) // Bean
                    return toJson(list.toArray(new BaseModel[list.size()]));
                else { // Bean
                    Object[] array = list.toArray(new Object[list.size()]);
                    return jsonArr(array, JsonHelper::beanToJson);
                }
            } else
                return "[]";
        } else if (obj instanceof Object[])
            return jsonArr((Object[]) obj, JsonHelper::toJson);
        else if (obj instanceof Enum) {
//			if (obj instanceof ValueEnmu) {
//				Serializable v = ((ValueEnmu) obj).getValue();
//
//				if (v instanceof String)
//					return "\"" + v + "\"";
//				else
//					return (String) v;
//			} else

            return "\"" + obj + "\"";
        } else { // 普通 Java Object
            List<String> arr = new ArrayList<>();
//            String clz = obj.getClass().toString();
//            System.out.println(clz);
//            if (clz.contains("javax.management.MBeanAttributeInfo"))
//                System.out.println(obj.getClass());

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                String key = field.getName();
                if (key.contains("this$"))
                    continue;

                Object _obj = null;

                try {
                    _obj = field.get(obj);
                } catch (IllegalAccessException e) {
                    LOGGER.warning(e);
                }

//                arr.add('\"' + key + "\":" + toJson(_obj)); // 不要递归了
                String value = "\"" + (_obj == null ? "" : jsonString_covert(_obj.toString())) + "\"";
                arr.add('\"' + key + "\":" + value);
            }

            return '{' + String.join(",", arr) + '}';
        }
    }

    /**
     * 输入任意类型数组，在 fn 作适当的转换，返回 JSON 字符串
     *
     * @param o  数组
     * @param fn 元素处理器，返回元素 JSON 字符串
     * @return 数组的 JSON 字符串
     */
    public static <T> String jsonArr(T[] o, Function<T, String> fn) {
        if (o.length == 0)
            return "[]";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < o.length; i++) {
            sb.append(fn.apply(o[i]));

            if (i != (o.length - 1))
                sb.append(", ");
        }

        return '[' + sb.toString() + ']';
    }

    /**
     * List 专为 JSON 字符串
     *
     * @param list 列表
     * @param fn   元素处理器，返回元素 JSON 字符串
     * @return 列表的 JSON 字符串
     */
    static <T> String eachList(List<T> list, Function<T, String> fn) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            sb.append(fn.apply(list.get(i)));
            if (i != (list.size() - 1))
                sb.append(", ");
        }

        return '[' + sb.toString() + ']';
    }

    /**
     * 输入一个 Map，将其转换为 JSON Str
     *
     * @param map 输入数据
     * @return JSON 字符串
     */
    private static String stringifyMap(Map<?, ?> map) {
        if (map == null)
            return null;

        if (map.size() == 0)
            return "{}";

        List<String> arr = new ArrayList<>();

        for (Object key : map.keySet()) {
            arr.add('\"' + key.toString() + "\":" + toJson(map.get(key)));
        }

        return '{' + String.join(",", arr) + '}';
    }

    /**
     * Bean 转换为 JSON 字符串
     * 传入任意一个 Java Bean 对象生成一个指定规格的字符串
     *
     * @param bean bean对象
     * @return String "{}"
     */
    private static String beanToJson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;

        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            LOGGER.warning(e);
        }

        if (props != null && props.length > 0) {
            for (PropertyDescriptor prop : props) {
                try {
                    String name = prop.getName();
                    // JDK Bean 问题：第一个字符串是单个字符的，不用转换为大写
                    name = firstCharToLowercase(name); // 永远第一个字符为小写，符合驼峰的风格
                    name = "\"" + name + "\"";

                    Object invoke = prop.getReadMethod().invoke(bean);
                    String value = toJson(invoke);

//                    if("ESeatNum".equals(props[i].getName()))
//                        System.out.println(props[i]);

                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                    System.out.println(prop.getName());
                    LOGGER.warning(e);
                }
            }

            json.setCharAt(json.length() - 1, '}');
        } else {
            // 无 getter/setter
            Field[] fields = bean.getClass().getFields();

            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    String name = "\"" + field.getName() + "\"";

                    try {
                        Object _value = field.get(bean);
                        String value = toJson(_value);

//						if ("photoInterval".equals(field.getName()))
//							LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>" + name+">>>>>"+_value);
                        json.append(name);
                        json.append(":");
                        json.append(value);
                        json.append(",");
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        LOGGER.warning(e);
                    }
                }

                json.setCharAt(json.length() - 1, '}');
            } else
                json.append("}");
        }

        return json.toString();
    }

    public static String firstCharToLowercase(String str) {
        if (str == null || str.length() == 0)
            return str;

        StringBuilder builder = new StringBuilder(str);
        builder.setCharAt(0, Character.toLowerCase(str.charAt(0)));

        return builder.toString();
    }


    /**
     * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t 换行用\r
     *
     * @param json 原 JSON 字符串
     * @return 格式化后美观的 JSON
     */
    public static String format(String json) {
        int level = 0;
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (level > 0 && '\n' == str.charAt(str.length() - 1))
                str.append(repeatStr("\t", "", level));

            switch (c) {
                case '{':
                case '[':
                    str.append(c).append("\n");
                    level++;

                    break;
                case ',':
                    if (json.charAt(i + 1) == '"')
                        str.append(c).append("\n"); // 后面必定是跟着 key 的双引号，但 其实 json 可以 key 不带双引号的
                    break;
                case '}':
                case ']':
                    str.append("\n");
                    level--;
                    str.append(repeatStr("\t", "", level));
                    str.append(c);
                    break;
                default:
                    str.append(c);
                    break;
            }
        }

        return str.toString();
    }

    /**
     * 删除 JSON 注释
     *
     * @param str 待处理的字符串
     * @return 删除注释后的字符串
     */
    public static String removeComment(String str) {
        return str.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
    }

    /**
     * 输出到 JSON 文本时候的换行
     *
     * @param str JSON 字符串
     * @return 转换后的字符串
     */
    public static String jsonString_covert(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r");
    }

    /**
     * 重复字符串 repeat 次并以 div 分隔
     *
     * @param str    要重复的字符串
     * @param div    字符串之间的分隔符
     * @param repeat 重复次数
     * @return 结果
     */
    public static String repeatStr(String str, String div, int repeat) {
        StringBuilder s = new StringBuilder();
        int i = 0;

        while (i++ < repeat) {
            s.append(str);

            if (i != repeat)
                s.append(div);
        }

        return s.toString();
    }

    /**
     * 转义注释和缩进
     *
     * @param str JSON 字符串
     * @return 转换后的字符串
     */
    public static String javaValue2jsonValue(String str) {
        return str.replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\\t");
    }

    /**
     * 清除 \r\n 字符
     *
     * @param str JSON 字符串
     * @return 转换后的字符串
     */
    public static String removeCr(String str) {
        return str.replaceAll("[\\r\\n]", "");
    }
}