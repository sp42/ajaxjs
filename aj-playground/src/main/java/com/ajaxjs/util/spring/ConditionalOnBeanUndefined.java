package com.ajaxjs.util.spring;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用注解{@link org.springframework.context.annotation.Bean}生成Bean时，使用该注解检测目标Bean是否已经存在
 * 
 * @author zifangsky
 * @date 2018/12/5 14:24
 * @since 1.0.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(BeanUndefinedCondition.class)
public @interface ConditionalOnBeanUndefined {
	Class<?>[] value() default {};
}