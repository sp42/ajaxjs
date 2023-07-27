package com.ajaxjs.framework.spring.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;
import java.util.List;

public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return converterType.equals(MyJsonConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // DAO 返回 null，直接输出前端会白屏
        // 这里强制输出为空对象
        if (body == null) {
            Class<?> clz = returnType.getParameterType();

            if (clz == List.class) {
                return Collections.emptyList();
            }
//            else if (IBaseModel.class.isAssignableFrom(clz)) {
//                // JavaBean
//                return new Object();
//            }

            return new Object();
        }

        return body;
    }
}
