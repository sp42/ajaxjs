package com.ajaxjs.framework.spring.easy_controller.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明这是一个控制器配置接口
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceBasedController {
	/**
	 * 对应的业务类
	 * 
	 * @return
	 */
	Class<?> serviceClass() default Object.class;
}