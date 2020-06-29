package com.ajaxjs.app;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.junit.Test;

import com.ajaxjs.framework.filter.BeanValidator;
import com.ajaxjs.sql.orm.BaseModel;

public class TestValidator {
	public static class News extends BaseModel {
		private static final long serialVersionUID = 1L;

		@NotNull(message = "名称不能为空")
		@Size(min = 2, max = 255, message = "长度应该介于3和255之间")
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
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
