package com.ajaxjs.spring.easy_controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记接口控制器里面的控制器方法
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerMethod {
	/**
	 * 注释
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 对应的业务类
	 * 
	 * @return
	 */
	Class<?> serviceClass() default Object.class;

	/**
	 * 对应的方法名称
	 * 
	 * @return
	 */
	String methodName() default "";
}
