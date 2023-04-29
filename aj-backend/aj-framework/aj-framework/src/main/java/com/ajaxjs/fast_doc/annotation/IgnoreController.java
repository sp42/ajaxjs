package com.ajaxjs.fast_doc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 忽略加入文档的控制器
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreController {
}
