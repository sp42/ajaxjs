package com.ajaxjs.data.jdbc_helper.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {
    /**
     * 表名
     */
    String value();

    /**
     * 创建实体后是否返回新建的 id
     */
    boolean isReturnNewlyId() default false;
}
