package com.ajaxjs.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL 语句的工厂
 * 
 * @author Frank Cheung
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlFactory {
	/**
	 * 必须为一个静态方法的名称，该方法输入一个 String 参数，返回一个 String 结果
	 * 
	 * @return 静态方法名称
	 */
	String value() default "";

	/**
	 * 方法所在的类。若为 Object.class 则指本类身上的
	 * 
	 * @return 方法所在的类
	 */
	Class<?> clz() default Object.class;
}
