package com.ajaxjs.validator;

import com.ajaxjs.util.CommonUtil;

public class BuiltinValidator {
	public static final Validator NOT_NULL_VALIDATOR = (value, field, ann) -> {
		if (value == null) {
			return field.getName() + " 不能为 null";
		} else if (value != null && value instanceof Number) {
			Number num = (Number) value;

			if (num.equals(0) || num.equals(0L))
				return field.getName() + " 不能为 null";
			else
				return null;
		} else
			return null;
	};

	public static final Validator NOT_BLANK_VALIDATOR = (value, field, ann) -> {
		if (value == null || CommonUtil.isEmptyString(value.toString())) {
			return field.getName() + " 不能为 为空";
		} else
			return null;
	};
}
