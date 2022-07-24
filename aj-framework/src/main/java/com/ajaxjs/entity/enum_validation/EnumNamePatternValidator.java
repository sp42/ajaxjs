package com.ajaxjs.entity.enum_validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义的枚举校验器
 */
public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {
	/**
	 * 校验的正则
	 */
	private Pattern pattern;

	/**
	 * 初始化
	 */
	@Override
	public void initialize(EnumNamePattern annotation) {
		try {
			pattern = Pattern.compile(annotation.regexp());
		} catch (PatternSyntaxException e) {
			throw new IllegalArgumentException("Given regex is invalid", e);
		}
	}

	/**
	 * 校验，是否通过验证
	 */
	@Override
	public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		Matcher m = pattern.matcher(value.name());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>22" + m.matches());
		return m.matches();
	}
}
