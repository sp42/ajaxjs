package com.ajaxjs.util.convert;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 转换复杂的对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ConvertComplexValue extends ConvertBasicValue {
    /**
     * 能够表示 Java Bean 的接口
     */
    private Class<?> beanClz;

    /**
     * 实例、单例
     */
    private volatile static ConvertComplexValue INSTANCE;

    /**
     * 获取 ConvertComplexValue 实例
     *
     * @return ConvertComplexValue 实例
     */
    public static ConvertComplexValue getConvertValue() {
        if (INSTANCE == null) {
            synchronized (ConvertComplexValue.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DefaultConvertValue();
                    INSTANCE.setBeanClz(IBaseModel.class);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * 输入 JSON 字符串，转换为 Map
     */
    protected abstract Map<String, Object> parseJsonAsMap(String value);

    /**
     * 输入 JSON String，转换为 Java Bean
     */
    protected abstract <T> T parseJsonMapAsBean(String value, Class<T> clz);

    /**
     * 输入 JSON String，转换为 List
     */
    protected abstract List<Map<String, Object>> parseList(String value);

    /**
     * 转换方法
     * 将传入的值转换为目标类型
     *
     * @param value 要转换的值
     * @param clz   目标类型
     * @return 转换后的值，如果转换不成功返回null
     */
    public Object convert(Object value, Class<?> clz) {
        Object converted = basicConvert(value, clz);

        if (converted == null) { // 如果返回 null 表示之前的转换不成功，下面继续进行转换
            if (clz == Map.class) {
                if (value instanceof String)
                    return parseJsonAsMap((String) value);
                else
                    LOGGER.warning("Only String value can be converted to Map, or the related handler not ready");
            } else if (beanClz != null && beanClz.isAssignableFrom(clz)) {
                // to Java Bean
                if (value instanceof String)
                    return parseJsonMapAsBean((String) value, clz);
                else if (value instanceof Map)
                    return EntityConvert.map2Bean((Map) value, clz, true);//  输入 Map，转换为 Bean
                else
                    LOGGER.warning("value: [{0}] type:[{1}] can not be converted to Java Bean, or the related handler not ready", value, value.getClass().getName());
            } else if (clz == List.class) {
                if (value instanceof String) {
                    String str = (String) value;

                    if (str.charAt(0) == '[' && str.charAt(1) == '{') // 判断为对象列表
                        return parseList(str);
                    else {
                        String[] arr = str.split(",");
                        List<String> list = new ArrayList<>();
                        Collections.addAll(list, arr);

                        return list;
                    }
                }

                LOGGER.warning("value: [{0}] type:[{1}] can not be converted to [2]", value, value.getClass().getName(), clz);
            }
        }

        return converted;
    }

    public static class DefaultConvertValue extends ConvertComplexValue {
        @Override
        protected Map<String, Object> parseJsonAsMap(String str) {
            return EntityConvert.json2map(str);
        }

        @Override
        protected <T> T parseJsonMapAsBean(String str, Class<T> clz) {
            return EntityConvert.json2bean(str, clz);
        }

        @Override
        protected List<Map<String, Object>> parseList(String str) {
            return EntityConvert.json2MapList(str);
        }
    }
}
