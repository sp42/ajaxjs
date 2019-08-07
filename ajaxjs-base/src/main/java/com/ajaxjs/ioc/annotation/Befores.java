package com.ajaxjs.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ajaxjs.ioc.Before;

/**
 * 前置器的容器
 * 
 * @author Frank Cheung
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Befores {
	/**
	 * 
	 * @return
	 */
	Before[] value();
}
