package com.ajaxjs.framework.spring.validator;


import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import java.lang.annotation.Annotation;

/**
 * 路径变量时进行参数验证
 */
public class PathVariableArgumentValidatorResolver extends PathVariableMethodArgumentResolver {
    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Object value = super.resolveName(name, parameter, request);
        // validateIfApplicable
        new ValidatorImpl(null).resolveAnnotations(parameter.getParameterAnnotations(), value);

        return value;
    }
}