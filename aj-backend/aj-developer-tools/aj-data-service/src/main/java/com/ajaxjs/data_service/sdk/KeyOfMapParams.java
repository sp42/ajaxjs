package com.ajaxjs.data_service.sdk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Mybatis 执行 SQL 的参数始终需要一个 map 结构，而 DAO 方法传 map 会比较麻烦，于是提供一个注解，指定一下 key 与
 * args[] 组成 map
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KeyOfMapParams {
	String[] value() default "";
}
