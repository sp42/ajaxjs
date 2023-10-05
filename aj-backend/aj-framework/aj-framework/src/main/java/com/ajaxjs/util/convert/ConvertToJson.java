package com.ajaxjs.util.convert;

import com.ajaxjs.framework.BaseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 序列化到 JSON
 */
public class ConvertToJson {
    /**
     * 对 Object 尝试类型检测，将其合适地转换为 JSON 字符串返回。
     * <p>
     * FIXME: Bean 列表 还不行；Mysql 新版驱动返回日期 LocalDate 会死循环
     *
     * @param obj 任意对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        Class<?> clz = obj.getClass();

        if (obj instanceof Boolean || obj instanceof Number) {
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
            return '\"' + jsonStringConvert((String) obj) + '\"';
        else if (obj instanceof Map)
            return stringifyMap((Map<?, ?>) obj);
        else if (obj instanceof List)
            return list2Json((List<?>) obj);
        else if (clz.isArray()) { // 数组的
            if (clz == int[].class || clz == Integer[].class)
                return jsonArr(clz == Integer[].class ? (Integer[]) obj : Arrays.stream((int[]) obj).boxed().toArray(Integer[]::new), String::valueOf);
            else if (clz == long[].class || clz == Long[].class)
                return jsonArr(clz == Long[].class ? (Long[]) obj : Arrays.stream((long[]) obj).boxed().toArray(Long[]::new), String::valueOf);
            else if (clz == String[].class)
                return jsonArr((String[]) obj, v -> "\"" + jsonStringConvert(v) + "\"");
            else if (obj instanceof Map[]) // map 子类都可以识别，效率差点
                return jsonArr((Map<?, ?>[]) obj, ConvertToJson::stringifyMap);
        }

        return null;
    }

    /**
     * List 转数组
     */
    static String list2Json(List<?> list) {
        if (list.size() > 0) {
            if (list.get(0) instanceof Integer) {
                return toJson(list.toArray(new Integer[0]));
            } else if (list.get(0) instanceof Long)
                return toJson(list.toArray(new Long[0]));
            else if (list.get(0) instanceof String)
                return toJson(list.toArray(new String[0]));
            else if (list.get(0) instanceof Map) // Map 类型的输出
                return toJson(list.toArray(new Map[0]));
            else if (list.get(0) instanceof BaseModel) // Bean
                return toJson(list.toArray(new BaseModel[0]));
            else { // Bean
                Object[] array = list.toArray(new Object[0]);
                return jsonArr(array, Convert::beanToJson);
            }
        } else
            return "[]";
    }

    /**
     * 输入任意类型数组，在 fn 作适当的转换，返回 JSON 字符串
     *
     * @param o  数组
     * @param fn 元素处理器，返回元素 JSON 字符串
     * @return 数组的 JSON 字符串
     */
    static <T> String jsonArr(T[] o, Function<T, String> fn) {
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
     * 将字符串中的特殊字符用转义符替换，用于生成 JSON 格式的字符串
     *
     * @param str JSON 字符串
     * @return 转换后的字符串
     */
    public static String jsonStringConvert(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r");
    }

    /**
     * 输入一个 Map，将其转换为 JSON Str
     *
     * @param map 输入数据
     * @return JSON 字符串
     */
    static String stringifyMap(Map<?, ?> map) {
        if (map == null)
            return null;

        if (map.size() == 0)
            return "{}";

        List<String> arr = new ArrayList<>(map.size());

        for (Object key : map.keySet())
            arr.add('\"' + key.toString() + "\":" + toJson(map.get(key)));

        return '{' + String.join(",", arr) + '}';
    }
}
