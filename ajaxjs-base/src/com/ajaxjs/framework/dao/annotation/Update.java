package com.ajaxjs.framework.dao.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Update {
  String value() default "autoUpdate";
  
  /**
   * 表名
   * @return
   */
  String tableName() default "";
}
