package com.ajaxjs.framework.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {
    /**
     * 表名
     */
    String value();
}
