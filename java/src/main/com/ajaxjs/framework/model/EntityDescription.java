package com.ajaxjs.framework.model;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * POJO 实体文档说明
 * 
 * @author frank
 *
 */
@Target(value={TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityDescription {
	/**
	 * 标记实体文档说明
	 * @return 字段说明
	 */
	public String doc() default "";
	
	/**
	 * 其他部分的文档，指定一个 jsp 路径。
	 * @return
	 */
	public String extraHTML_path() default "";
}
