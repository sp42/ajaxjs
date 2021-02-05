package com.ajaxjs.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@FunctionalInterface
public interface Validator {
	/**
	 * 执行验证
	 * 
	 * @param value Bean 字段的值
	 * @param field Bean 字段对象
	 * @param ann   Bean 字段上面的注解
	 * @return 错误信息，如果为 null 表示完全通过
	 */
	public String valid(Object value, Field field, Annotation ann);
}
