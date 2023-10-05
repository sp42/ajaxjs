package com.ajaxjs.util.convert;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.Clazz;
import com.ajaxjs.util.reflect.NewInstance;
import com.ajaxjs.util.reflect.Types;
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
 * Bean、Map、Json 三者的相互转换
 */
public class Convert {
    public static final LogHelper LOGGER = LogHelper.getLog(Convert.class);

    /**
     * Bean 转为 Map
     *
     * @param bean 实体 bean 对象
     * @return Map 对象
     */
    public static <T> Map<String, Object> bean2Map(T bean) {
        Map<String, Object> map = new HashMap<>();
        Clazz.eachField(bean, (k, v, property) -> map.put(k, v));
        Clazz.eachFields(bean, map::put); // 处理无 getter/setter 的

        return map;
    }

    /**
     * 简单的 bean（没 getter/setter 的）转换为 Map。JSP 的 El 表达式会使用到
     */
    public static Map<String, Object> simpleBean2Map(Object bean) {
        Map<String, Object> map = new HashMap<>();
        Clazz.eachFields(bean, map::put);

        return map;
    }

    public static List<Map<String, Object>> simpleBean2Map(List<?> beanList) {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Object bean : beanList)
            list.add(simpleBean2Map(bean));

        return list;
    }

    /**
     * Bean 转换为 JSON 字符串
     * 传入任意一个 Java Bean 对象生成一个指定规格的字符串
     *
     * @param bean Java bean 对象
     * @return JSON
     */
    public static String beanToJson(Object bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        PropertyDescriptor[] props = null;

        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            JsonHelper.LOGGER.warning(e);
        }

        if (!ObjectUtils.isEmpty(props)) {
            Clazz.eachField(bean, props, (String key, Object value, PropertyDescriptor property) -> {
                // JDK Bean 问题：第一个字符串是单个字符的，不用转换为大写
                key = firstCharToLowercase(key); // 永远第一个字符为小写，符合驼峰的风格
                key = "\"" + key + "\"";

                String _value = JsonHelper.toJson(value);

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
                    String value = JsonHelper.toJson(_value);

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
    public static String firstCharToLowercase(String str) {
        if (str == null || str.length() == 0)
            return str;

        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toLowerCase(str.charAt(0)));

        return sb.toString();
    }

    /**
     * 把字符串还原为 Java 里面的真实值，如 "true"--true,"123"--123,"null"--null
     *
     * @param value 字符串的值
     * @return Java 里面的值
     */
    public static Object toJavaValue(String value) {
        if (value == null)
            return null;

        value = value.trim();

        if ("".equals(value))
            return "";

        if ("null".equals(value))
            return null;

        if ("true".equalsIgnoreCase(value))
            return true;

        if ("false".equalsIgnoreCase(value))
            return false;

        // try 比较耗资源，先检查一下
        if (value.charAt(0) == '-' || (value.charAt(0) >= '0' && value.charAt(0) <= '9'))
            try {
                int int_value = Integer.parseInt(value);

                if ((String.valueOf(int_value)).equals(value)) // 判断为整形
                    return int_value;
            } catch (NumberFormatException e) {// 不能转换为数字
                try {
                    long long_value = Long.parseLong(value);
                    if ((String.valueOf(long_value)).equals(value)) // 判断为整形
                        return long_value;
                } catch (NumberFormatException e1) {
                    if (value.matches("[0-9]{1,13}(\\.[0-9]*)?"))
                        return Double.parseDouble(value);
                }
            }

        return value;
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
    @SuppressWarnings("unchecked")
    public static <T> T map2Bean(Map<String, ?> map, Class<T> clz, boolean isTransform, boolean isChild) {
        T bean = NewInstance.newInstance(clz); // 创建 Bean

        if (map == null || bean == null)
            return bean;

        Clazz.eachField(bean, (String key, Object v /* always null */, PropertyDescriptor property) -> {
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
                    List<Map<String, Object>> oldValue = value instanceof String ? JsonHelper.parseList((String) value) : (List<Map<String, Object>>) value;
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
                    value = ConvertValue.getConvertValue().to(value, t);
            }

            try {
                property.getWriteMethod().invoke(bean, value);

                if (isChild)
                    child();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                if (e instanceof IllegalArgumentException) {
                    LOGGER.warning("Method:[0], JSON in DB：[1]", property.getWriteMethod(), IBaseModel.class.isAssignableFrom(t));
                    LOGGER.warning("[{0}] 参数类型不匹配，期望类型是[{1}], 输入值是 [{2}], 输入类型是 [{3}]", key, t, value, value.getClass().toString());
                } else
                    LOGGER.warning(e);
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
                value = ConvertValue.getConvertValue().to(value, t);

            try {
                field.set(bean, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                LOGGER.warning(e);
            }
        });

        return bean;
    }

    /**
     * 子对象
     */
    static void child() {
        //					for (String mKey : map.keySet()) {
        //						if (mKey.contains(key + '_')) {
        //							Method getter = property.getReadMethod(), setter = property.getWriteMethod();// 得到对应的 setter
        //																											// 方法
        //
        //							Object subBean = getter.invoke(bean);
        //							String subBeanKey = mKey.replaceAll(key + '_', "");
        //
        //							if (subBean != null) {// 已有子 bean
        //								if (map.get(mKey) != null) // null 值不用处理
        //									ReflectUtil.setProperty(subBean, subBeanKey, map.get(mKey));
        //							} else { // map2bean
        //								Map<String, Object> subMap = new HashMap<>();
        //								subMap.put(subBeanKey, map.get(mKey));
        //								subBean = map2Bean(subMap, setter.getParameterTypes()[0], isTransform);
        //								setter.invoke(bean, subBean); // 保存新建的 bean
        //							}
        //						}
        //					}
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
}
