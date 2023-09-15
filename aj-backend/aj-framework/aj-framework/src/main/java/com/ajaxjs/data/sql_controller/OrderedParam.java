package com.ajaxjs.data.sql_controller;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应 SQL 里面的 ? 插值参数，按照顺序的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface OrderedParam {
//    String value() default "";
}