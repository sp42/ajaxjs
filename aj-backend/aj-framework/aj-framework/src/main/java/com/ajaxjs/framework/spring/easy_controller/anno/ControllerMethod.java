package com.ajaxjs.framework.spring.easy_controller.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记接口控制器里面的控制器方法
 *
 * @author Frank Cheung sp42@qq.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerMethod {
    /**
     * 注释
     *
     * @return 注释
     */
    String value() default "";

    /**
     * 详细简介（可选的）
     *
     * @return 详细简介
     */
    String description() default "";

    /**
     * 图片（可选的）
     *
     * @return 图片
     */
    String image() default "";

    /**
     * 对应的业务类
     *
     * @return 业务类
     */
    Class<?> serviceClass() default Object.class;

    /**
     * 对应的方法名称
     *
     * @return 对应的方法名称
     */
    String methodName() default "";
}
