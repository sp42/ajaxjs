package com.ajaxjs.framework.entity.enum_validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 对 String 进行枚举的校验
 * 
 * 用法：
 * <pre>  
@ValueOfEnum(enumClass = CustomerType.class)
private String customerTypeString;
</pre>
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
	Class<? extends Enum<?>> enumClass();

	String message() default "must be any of enum {enumClass}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}