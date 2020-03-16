package com.ajaxjs.mvc.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为 SQL 或操作行为需要记录的，以便以后的审核
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlAuditing {
}
