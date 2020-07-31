package com.ajaxjs.validator;

import static org.junit.Assert.assertNotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.junit.Test;

public class TestValidator {
	class News {
		@NotNull
		private long id;

		@NotBlank(message = "请输入名称")
		private String name;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Test
	public void test() {
		News news = new News();
		String result[] = BeanValidator.validate(news);
		assertNotNull(result);

		System.out.println(result[0]);
		System.out.println(result[1]);
	}
}
