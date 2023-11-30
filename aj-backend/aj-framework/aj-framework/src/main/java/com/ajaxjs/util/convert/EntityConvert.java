package com.ajaxjs.util.convert;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.jsonparser.JsonParseException;
import com.ajaxjs.jsonparser.syntax.FMS;
import com.ajaxjs.util.reflect.Clazz;
import com.ajaxjs.util.reflect.NewInstance;
import com.ajaxjs.util.reflect.Types;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体转换
 * Bean、Map、Json 三者的相互转换
 */
@Slf4j
public class EntityConvert {
    /**
     * Bean 转为 Map
     *
     * @param bean Java Bean 实体
     * @return Map 实体
     */
    public static Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> map = new HashMap<>();
        Clazz.eachField(bean, (k, v, property) -> map.put(k, v));
        Clazz.eachFields(bean, map::put); // 处理无 getter/setter 的

        return map;
    }

    /**
     * 简单的 bean（没 getter/setter 的）转换为 Map。JSP 的 El 表达式会使用到
     *
     * @param bean Java Bean 实体
     * @return Map 实体
     */
    public static Map<String, Object> bean2MapSimple(Object bean) {
        Map<String, Object> map = new HashMap<>();
        Clazz.eachFields(bean, map::put);

        return map;
    }

    /**
     * 将 List<?> 类型的 beanList 转换成 List<Map<String, Object>> 类型
     *
     * @param beanList 要转换的 beanList
     * @return 转换后的 List<Map<String, Object>> 类型
     */
    public static List<Map<String, Object>> bean2MapSimple(List<?> beanList) {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Object bean : beanList)
            list.add(bean2MapSimple(bean));

        return list;
    }


    /**
     * Map 实体 转为 Java Bean 实体
     *
     * @param map         Map 实，原始数据
     * @param clz         实体 Java Bean 的类
     * @param isTransform 是否尝试转换值
     * @param isChild     是否处理子对象
     * @return 实体 bean 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T map2Bean(Map<String, ?> map, Class<T> clz, boolean isTransform, boolean isChild) {
        T bean = NewInstance.newInstance(clz); // 创建 Bean

        if (map == null || bean == null)
            return bean;

        Clazz.eachField(bean, (String key, Object _v /* always null */, PropertyDescriptor property) -> {
            // 字段名第一个为大写，不符合驼峰风格。数据库字段应避免 s_xxx 的命名。这里特殊处理
            char firstChar = key.charAt(0);

            if (Character.isUpperCase(firstChar)) // 取不到，尝试手动转换
                key = key.replaceFirst("^[A-Z]", String.valueOf(Character.toLowerCase(firstChar)));

            Object value = map.get(key);// Bean 的字段从 Map 中取

            if (value == null)// null 是不会传入 bean 的
                return;

            Class<?> t = property.getPropertyType(); // Bean 值的类型，这是期望传入的类型，也就 setter 参数的类型

            if (t == List.class) { // List 容器类，要处理里面的泛型
                Type[] genericReturnType = Types.getGenericReturnType(property.getReadMethod());
                Class<?> realT = Types.type2class(genericReturnType[0]);

                List<?> _list = null;
                if (value instanceof List)
                    _list = (List<?>) value;

                if (realT.equals(String.class) && _list != null) {
                    // List<String> 不用处理
                } else {
                    List<Map<String, Object>> oldValue = value instanceof String ? json2MapList((String) value) : (List<Map<String, Object>>) value;
                    List<Object> newList = new ArrayList<>(oldValue.size());

                    // 转换一个新 list
                    for (Map<String, Object> _map : oldValue) {
                        Object _bean = map2Bean(_map, realT, true);
                        newList.add(_bean);
                    }

                    value = newList;
                }
            } else {
                if (isTransform && t != value.getClass()) // 类型相同，直接传入；类型不相同，开始转换
                    value = ConvertComplexValue.getConvertValue().convert(value, t);
            }

            try {
                property.getWriteMethod().invoke(bean, value);

                if (isChild)
                    child();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                if (e instanceof IllegalArgumentException) {
                    log.warn("Method:[{}], JSON in DB：[{}]", property.getWriteMethod(), IBaseModel.class.isAssignableFrom(t));
                    log.warn("[{}] 参数类型不匹配，期望类型是[{}], 输入值是 [{}], 输入类型是 [{}]", key, t, value, value.getClass().toString());
                } else
                    log.warn("ERROR>>", e);
            }
        });

        // 处理无 getter/setter 的
        Clazz.eachFields2(clz, (key, field) -> {
            Object value = map.get(key);

            if (value == null)
                return;

            Class<?> t = field.getType();
            // TODO list
            if (isTransform && t != value.getClass()) // 类型相同，直接传入；类型不相同，开始转换
                value = ConvertComplexValue.getConvertValue().convert(value, t);

            try {
                field.set(bean, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.warn("ERROR>>", e);
            }
        });

        return bean;
    }

    /**
     * 子对象
     */
    static void child() {
//        for (String mKey : map.keySet()) {
//            if (mKey.contains(key + '_')) {
//                Method getter = property.getReadMethod(), setter = property.getWriteMethod();// 得到对应的 setter
//                // 方法
//
//                Object subBean = getter.invoke(bean);
//                String subBeanKey = mKey.replaceAll(key + '_', "");
//
//                if (subBean != null) {// 已有子 bean
//                    if (map.get(mKey) != null) // null 值不用处理
//                        ReflectUtil.setProperty(subBean, subBeanKey, map.get(mKey));
//                } else { // map2bean
//                    Map<String, Object> subMap = new HashMap<>();
//                    subMap.put(subBeanKey, map.get(mKey));
//                    subBean = map2Bean(subMap, setter.getParameterTypes()[0], isTransform);
//                    setter.invoke(bean, subBean); // 保存新建的 bean
//                }
//            }
//        }
    }

    public static <T> T map2Bean(Map<String, ?> map, Class<T> clz, boolean isTransform) {
        return map2Bean(map, clz, isTransform, true);
    }

    /**
     * Map 转实体
     *
     * @param map 原始数据
     * @param clz 实体 bean 的类
     * @return 实体 bean 对象
     */
    public static <T> T map2Bean(Map<String, ?> map, Class<T> clz) {
        return map2Bean(map, clz, false);
    }

    /**
     * JSON 序列化：Java Bean 转换为 JSON 字符串
     *
     * @param bean Java Bean 实体
     * @return JSON 字符串
     */
    public static String beanToJson(Object bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        PropertyDescriptor[] props = null;

        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.warn("ERROR>>", e);
        }

        if (!ObjectUtils.isEmpty(props)) {
            Clazz.eachField(bean, props, (String key, Object value, PropertyDescriptor property) -> {
                // JDK Bean 问题：第一个字符串是单个字符的，不用转换为大写
                key = firstCharToLowercase(key); // 永远第一个字符为小写，符合驼峰的风格
                key = "\"" + key + "\"";

                String _value = ConvertToJson.toJson(value);

                sb.append(key);
                sb.append(":");
                sb.append(_value);
                sb.append(",");
            });

            sb.setCharAt(sb.length() - 1, '}');

        } else {
            // 无 getter/setter
            if (!ObjectUtils.isEmpty(bean.getClass().getFields())) {
                Clazz.eachFields(bean, (name, _value) -> {
                    name = "\"" + name + "\"";
                    String value = ConvertToJson.toJson(_value);

                    sb.append(name);
                    sb.append(":");
                    sb.append(value);
                    sb.append(",");
                });

                sb.setCharAt(sb.length() - 1, '}');
            } else
                sb.append("}");
        }

        return sb.toString();
    }

    /**
     * 将字符串的首字母转换为小写
     *
     * @param str 要转换的字符串
     * @return 首字母转换为小写后的字符串
     */
    private static String firstCharToLowercase(String str) {
        if (str == null || str.length() == 0)
            return str;

        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toLowerCase(str.charAt(0)));

        return sb.toString();
    }

    /**
     * JSON 序列化：Map 实体 转换为 JSON 字符串
     * 这个本框架提供支持，无须第三方库支持
     *
     * @param map Map 实体
     * @return JSON 字符串
     */
    public static String map2json(Map<?, ?> map) {
        if (map == null)
            return null;

        if (map.size() == 0)
            return "{}";

        List<String> arr = new ArrayList<>(map.size());

        for (Object key : map.keySet())
            arr.add('\"' + key.toString() + "\":" + ConvertToJson.toJson(map.get(key)));

        return '{' + String.join(",", arr) + '}';
    }

    /**
     * JSON 反序列化：解析 JSON 字符串为 Map
     * 这个方法需要 JSON 工具支持
     *
     * @param json JSON 字符串
     * @return Map 实体
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2map(String json) {
        return (Map<String, Object>) parse(json);
    }

    /**
     * JSON 反序列化：解析 JSON 字符串为 Bean
     * 这个方法需要 JSON 工具支持
     *
     * @param json JSON 字符串
     * @param clz  实体 bean 的类
     * @return Java Bean 实体
     */
    public static <T> T json2bean(String json, Class<T> clz) {
        Map<String, Object> map = json2map(json);

        return map2Bean(map, clz, true); // 应该为 false
    }

    /**
     * 解析 JSON 为 Map 或 List
     *
     * @param str JSON 字符串
     * @return Map 或 List
     */
    private static Object parse(String str) {
        str = ConvertToJson.removeCr(str);

        try {
            return new FMS(str).parse();
        } catch (JsonParseException e) {
            log.warn("JSON 解析错误：" + str);
            throw e;
        }
    }

    /**
     * 解析 JSON 字符串为 List
     *
     * @param str JSON 字符串
     * @return List
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> json2MapList(String str) {
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
    public static <T> List<T> json2BeanList(String json, Class<T> clz) {
        List<T> list = new ArrayList<>();
        List<Map<String, Object>> maps = json2MapList(json);

        for (Map<String, Object> map : maps) {
            T bean = map2Bean(map, clz);
            list.add(bean);
        }

        return list;
    }
}
