package com.ajaxjs.framework.spring.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;
import java.util.List;

/**
 * DAO 返回 null，直接输出前端会白屏 这里强制输出为空对象
 */
@RestControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return converterType.equals(MyJsonConverter.class);
    }

    /**
     * 找不到数据，查询为空
     */
    public static final Object NULL_DATA = new Object();

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            Class<?> clz = returnType.getParameterType();

            if (clz == List.class || clz.isArray())
                return Collections.emptyList();

//            else if (IBaseModel.class.isAssignableFrom(clz)) {
//                // JavaBean
//                return new Object();
//            }

            return NULL_DATA;
        }

        return body;
    }
}