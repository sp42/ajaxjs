package com.ajaxjs.util.spring.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//@RestControllerAdvice
@Deprecated
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter params, Class<? extends HttpMessageConverter<?>> clz) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter params, MediaType t, Class<? extends HttpMessageConverter<?>> clz, ServerHttpRequest req, ServerHttpResponse resp) {
		System.out.println("---------------------" + o);
//		if (o instanceof Map) {
//			ResponseResult result = new ResponseResult();
////			result.setMessage("获取无人机资源统计数字");
//			result.setData(JsonHelper.toJson(o));
//
//			return result.toString();
//		}

		return o;
//		if (o instanceof String)
//			return objectMapper.writeValueAsString(ResultData.ok(o));
//
//		return ResultData.ok(o);
	}
}
