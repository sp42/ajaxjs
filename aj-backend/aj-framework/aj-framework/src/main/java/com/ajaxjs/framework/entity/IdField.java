package com.ajaxjs.framework.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IdField {
    /**
     * Id 是哪个字段？
     */
    String value();
}
