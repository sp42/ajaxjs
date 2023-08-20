package com.ajaxjs.framework.spring.validator.custom;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdCard {
    String message() default "身份证号格式不正确";

    boolean required() default true;
}