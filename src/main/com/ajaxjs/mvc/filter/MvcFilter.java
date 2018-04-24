package com.ajaxjs.mvc.filter;

import java.lang.annotation.Target;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MvcFilter {
	Class<? extends FilterAction>[] before();
}
