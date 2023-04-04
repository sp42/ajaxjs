package com.ajaxjs.framework.spring.easy_controller.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器路径，不应放在控制器上。控制器直接用 @RequestMapping 即可。值跟 @RequestMapping 的一致
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PathForDoc {
	/**
	 * 说明
	 * 
	 * @return
	 */
	String value() default "";
}
