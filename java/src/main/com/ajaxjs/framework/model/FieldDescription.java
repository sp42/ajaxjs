package com.ajaxjs.framework.model;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * POJO 字段说明
 * 
 * @author frank
 *
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDescription {
	/**
	 * 标记字段说明
	 * @return 字段说明
	 */
	public String doc() default "";
}
