package com.ajaxjs.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ajaxjs.util.CommonUtil;

/**
 * 内建的验证器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class BuiltinValidator {
	public static final Validator NOT_NULL_VALIDATOR = (value, field, ann) -> {
		if (value == null) {
			NotNull n = (NotNull) ann;
			return n.message() != null ? n.message() : field.getName() + " 不能为 null";
		} else if (value != null && value instanceof Number) {
			Number num = (Number) value;

			if (num.equals(0) || num.equals(0L)) {
				NotNull n = (NotNull) ann;
				return n.message() != null ? n.message() : field.getName() + " 不能为 null";
			} else
				return null;
		} else
			return null;
	};

	public static final Validator NOT_BLANK_VALIDATOR = (value, field, ann) -> {
		if (value == null || CommonUtil.isEmptyString(value.toString())) {
			NotBlank n = (NotBlank) ann;
			return n.message() != null ? n.message() : field.getName() + " 不能为 为空";
		} else
			return null;
	};

	public static final Validator NOT_EMAIL_VALIDATOR = (Object value, Field field, Annotation ann) -> {
		String result = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		if (!value.toString().matches(result)) {
			NotEmail n = (NotEmail) ann;
			return n.message() != null ? n.message() : field.getName() + n.message();
		} else {
			return null;
		}
	};
}
