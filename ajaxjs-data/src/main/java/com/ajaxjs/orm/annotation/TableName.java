package com.ajaxjs.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DAO 中表示表名
 * 
 * @author Frank Cheung
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {
	/**
	 * SQL 表名
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 实体类
	 * 
	 * @return
	 */
	Class<?> beanClass() default Object.class;
}
