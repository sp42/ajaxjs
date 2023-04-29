package com.ajaxjs.framework.spring.easy_controller;

import java.lang.reflect.Method;

/**
 * 控制器的前置拦截器
 */
public interface BeforeInterceptor {
    boolean before(Method beanMethod, Object[] args);
}
