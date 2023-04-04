package com.ajaxjs.framework.spring.easy_controller.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 例子
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Example {
	/**
	 * 例子，JSON 格式
	 * 
	 * @return
	 */
	String value() default "";
}
