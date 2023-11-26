package com.ajaxjs.iam.resource_server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    public static <T> T jsonStr2Bean(String str, Class<T> clz) {
        try {
            return new ObjectMapper().readValue(str, clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bean2json(Object bean) {
        try {
            return new ObjectMapper().writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
