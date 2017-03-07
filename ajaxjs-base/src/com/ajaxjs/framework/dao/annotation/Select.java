package com.ajaxjs.framework.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
	String value();
	
	/**
	 * 是否分页
	 * @return
	 */
	boolean isPageList() default false;
	
	/**
	 * 是否需要条件查询
	 * @return
	 */
	boolean isQuerySupport() default false;
}
