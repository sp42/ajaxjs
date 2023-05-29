package com.ajaxjs.framework.spring.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

/**
 * Bean 校验拦截器
 */
public class BeanValidation {
    @Autowired
    LocalValidatorFactoryBean v;

    public boolean before(Method beanMethod, Object[] args) {
        Parameter[] parameters = beanMethod.getParameters();
        int i = 0;

        for (Parameter parameter : parameters) {
            Annotation[] annotations = parameter.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation instanceof Valid) {
                    Validator validator = v.getValidator();
                    Set<ConstraintViolation<Object>> violations = validator.validate(args[i]);

                    if (!CollectionUtils.isEmpty(violations)) {
                        StringBuilder sb = new StringBuilder();

                        for (ConstraintViolation<Object> v : violations) {
                            sb.append("输入字段[").append(v.getPropertyPath()).append("]，当前值[").append(v.getInvalidValue()).append("]，校验失败原因[");
                            sb.append(v.getMessage()).append("];");
                        }

                        sb.append("请检查后再提交");

                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }

            i++;
        }

        return true;
    }
}
