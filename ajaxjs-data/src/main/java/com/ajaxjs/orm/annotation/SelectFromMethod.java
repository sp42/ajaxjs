package com.ajaxjs.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelectFromMethod {
	/**
	 * 本类身上的一个静态方法名称
	 * 
	 * @return 静态方法名称
	 */
	String value() default "";
}
