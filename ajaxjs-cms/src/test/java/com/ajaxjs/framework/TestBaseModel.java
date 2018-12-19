package com.ajaxjs.framework;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import com.ajaxjs.framework.service.ValidationService;

public class TestBaseModel {
 
	@Test
	public void testValid() {
		Validator v = ValidationService.getValidator();
		News news = new News();
		news.setName("标题");
		Set<ConstraintViolation<News>> result = v.validate(news);
		assertFalse("应该不通过校验", result.isEmpty());

		News news2 = new News();
		result = v.validate(news2);
		assertTrue("应该不通过校验", !result.isEmpty());

		for (ConstraintViolation<News> r : result) {
			r.getMessage();
			r.getPropertyPath();
		}
	}
}
