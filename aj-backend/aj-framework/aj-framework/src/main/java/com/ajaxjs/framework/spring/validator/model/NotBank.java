package com.ajaxjs.framework.spring.validator.model;


import java.lang.annotation.*;

/**
 * @author volicy.xu
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBank {

    String message() default ValidatorConstant.PARAM_ERROR_MESSAGE;

    boolean required() default true;
}
