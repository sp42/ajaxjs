package com.ajaxjs.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTools {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<Map<String, Object>>() {
    };

    public static Map<String, Object> json2map(String json) {
        try {
            return objectMapper.readValue(json, MAP_TYPE_REF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final TypeReference<List<Map<String, Object>>> LIST_MAP_TYPE_REF = new TypeReference<List<Map<String, Object>>>() {
    };

    public static List<Map<String, Object>> jsonToListOfMap(String json) {
        try {
            return objectMapper.readValue(json, LIST_MAP_TYPE_REF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String beanToJson(Object bean)  {
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 Java Bean 转换为 Map
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> bean2map(Object bean) {
        return objectMapper.convertValue(bean, HashMap.class);
    }

    /**
     * 将 Map 转换为 Java Bean
     *
     * @param map
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T map2bean(Map<String, Object> map, Class<T> clz) {
        return objectMapper.convertValue(map, clz);
    }
}
