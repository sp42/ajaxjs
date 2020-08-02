package com.ajaxjs.validator;

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
}
