package com.ajaxjs.data.sql_controller;


import java.lang.annotation.*;

/**
 * 绑定 SQL XML
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlBinding {
    /**
     * 对应 SQL XML 里面的 id
     *
     * @return id
     */
    String value() default "";

    /**
     * SQL 语句
     *
     * @return SQL 语句
     */
    String sql() default "";

    /**
     * 是否前置拦截的方法
     *
     * @return true = 是前置拦截的方法
     */
    boolean isBefore() default false;

    String before() default "";
}