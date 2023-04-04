package com.ajaxjs.framework.spring.easy_controller.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Info {
	/**
	 * 说明
	 * 
	 * @return
	 */
	String value() default "";

	String description() default "";
}
