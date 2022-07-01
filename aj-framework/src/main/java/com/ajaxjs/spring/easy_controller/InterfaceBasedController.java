package com.ajaxjs.spring.easy_controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceBasedController {
	/**
	 * 对应的业务类
	 * 
	 * @return
	 */
	Class<?> serviceClass() default Object.class;
}