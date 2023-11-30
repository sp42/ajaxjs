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
    /**
     * 重写的方法，用于判断是否支持指定的返回类型和消息转换器类型。
     *
     * @param returnType    返回类型的方法参数
     * @param converterType 指定的 HTTP 消息转换器类型
     * @return 如果消息转换器类型为 MyJsonConverter 类则返回 true，否则返回 false
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return converterType.equals(MyJsonConverter.class);
    }

    /**
     * 找不到数据，查询为空
     */
    public static final Object NULL_DATA = new Object();

    /**
     * 在写入消息体之前执行的操作
     *
     * @param body                  要写入的消息体
     * @param returnType            返回类型
     * @param selectedContentType   选择的媒体类型
     * @param selectedConverterType 选择的 HttpMessageConverter 类型
     * @param request               服务器 HTTP 请求对象
     * @param response              服务器H TTP 响应对象
     * @return 返回写入的消息体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果消息体为 null
        if (body == null) {
            // 获取返回类型
            Class<?> clz = returnType.getParameterType();

            // 如果返回类型为List或数组
            if (clz == List.class || clz.isArray())
                return Collections.emptyList();

            // 返回空数据
            return NULL_DATA;
        }

        // 返回消息体
        return body;
    }

}