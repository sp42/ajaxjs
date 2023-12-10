package com.ajaxjs.iam.resource_server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    /**
     * 将 JSON 字符串转换为 Java 对象
     *
     * @param str JSON 字符串
     * @param clz Java 对象的类型
     * @return 转换后的 Java 对象
     */
    public static <T> T jsonStr2Bean(String str, Class<T> clz) {
        try {
            return new ObjectMapper().readValue(str, clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param bean 要转换的对象
     * @return 转换后的 JSON 字符串
     * @throws RuntimeException JSON 处理异常时抛出运行时异常
     */
    public static String bean2json(Object bean) {
        try {
            return new ObjectMapper().writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
