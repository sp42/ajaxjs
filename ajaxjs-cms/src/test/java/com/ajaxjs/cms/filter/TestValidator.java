package com.ajaxjs.cms.filter;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import com.ajaxjs.framework.BaseModel;

public class TestValidator {
	public static class News extends BaseModel {
		private static final long serialVersionUID = 1L;
	}

	@Test
	public void TestValidation() {
		Validator v = BeanValidator.AVF.getValidator();
		News news = new News();
		news.setName("标题");
		Set<ConstraintViolation<News>> result = v.validate(news);
		assertTrue("应该通过校验", result.isEmpty());

		News news2 = new News();
		result = v.validate(news2);
		assertTrue("应该不通过校验", !result.isEmpty());

		System.out.println(result.size());
		for (ConstraintViolation<News> r : result) {
			System.out.println(r.getMessage());// 什么错？
			System.out.println(r.getPropertyPath());// 哪个字段错？
		}
	}
}
