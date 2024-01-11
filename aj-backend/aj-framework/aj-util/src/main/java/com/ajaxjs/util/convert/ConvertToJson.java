package com.ajaxjs.util.convert;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.util.DateUtil;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
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
        if (obj == null)
            return null;

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
        else if (obj instanceof Date)
            return '\"' + DateUtil.simpleDateFormatFactory(DateUtil.DATE_FORMAT).format((Date) obj) + '\"';
        else if (obj instanceof LocalDateTime)  /* 新版 MySQL Driver 返回 LocalDateTime 类型 */
            return '\"' + ((LocalDateTime) obj).format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) + '\"';
        else if (obj instanceof BaseModel || obj instanceof IBaseModel)
            return EntityConvert.beanToJson(obj);
        else if (obj instanceof Map)
            return EntityConvert.map2json((Map<?, ?>) obj);
        else if (obj instanceof List)
            return list2Json((List<?>) obj);
        else if (obj.getClass().isArray()) { // 数组的
            Class<?> clz = obj.getClass();

            if (clz == int[].class || clz == Integer[].class)
                return jsonArr(clz == Integer[].class ? (Integer[]) obj : Arrays.stream((int[]) obj).boxed().toArray(Integer[]::new), String::valueOf);
            else if (clz == long[].class || clz == Long[].class)
                return jsonArr(clz == Long[].class ? (Long[]) obj : Arrays.stream((long[]) obj).boxed().toArray(Long[]::new), String::valueOf);
            else if (clz == String[].class)
                return jsonArr((String[]) obj, v -> "\"" + jsonStringConvert(v) + "\"");
            else if (obj instanceof Map[]) // map 子类都可以识别，效率差点
                return jsonArr((Map<?, ?>[]) obj, EntityConvert::map2json);
            else if (obj instanceof BaseModel[])
                return jsonArr((BaseModel[]) obj, EntityConvert::beanToJson);
            else if (obj instanceof IBaseModel[])
                return jsonArr((IBaseModel[]) obj, EntityConvert::beanToJson);
            else if (obj instanceof Object[])
                return jsonArr((Object[]) obj, ConvertToJson::toJson);
        } else if (obj instanceof Enum) {
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
                    e.printStackTrace();
                }

//                arr.add('\"' + key + "\":" + toJson(_obj)); // 不要递归了
                String value = "\"" + (_obj == null ? "" : jsonStringConvert(_obj.toString())) + "\"";
                arr.add('\"' + key + "\":" + value);
            }

            return '{' + String.join(",", arr) + '}';
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
                return jsonArr(array, EntityConvert::beanToJson);
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
                    if (json.charAt(i + 1) == '"' || json.charAt(i - 1) == '"') {// 后面必定是跟着 key 的双引号，但 其实 json 可以 key 不带双引号的
                        str.append(c).append("\n");
                        break;
                    }

                    str.append(c);
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
     * 重复字符串 repeat 次并以 div 分隔
     *
     * @param str    要重复的字符串
     * @param div    字符串之间的分隔符
     * @param repeat 重复次数
     * @return 结果
     */
    static String repeatStr(String str, String div, int repeat) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        while (i++ < repeat) {
            sb.append(str);

            if (i != repeat)
                sb.append(div);
        }

        return sb.toString();
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

    /**
     * 删除 JS 注释
     *
     * @param str 待处理的字符串
     * @return 删除注释后的字符串
     */
    public static String removeComment(String str) {
        return str.replaceAll("//[^\\n]*|/\\*([^*^/]*|[*^/]*|[^*/]*)*\\*+/", "");
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

}
