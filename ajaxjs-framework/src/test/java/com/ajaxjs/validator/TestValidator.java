package com.ajaxjs.validator;

import static org.junit.Assert.assertNotNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.junit.Test;

public class TestValidator {
	class News {
		@NotNull
		private long id;

		@NotNull(message = "名称不能为空")
		@Size(min = 2, max = 255, message = "长度应该介于3和255之间")
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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@NotEmail(message = "请注意规范")
		private String email;
	}

	@Test
	public void test() {
		News news = new News();
		news.setEmail("dffsd");
		
		String result[] = BeanValidator.validate(news);
		assertNotNull(result);

		System.out.println(result[0]);
		System.out.println(result[1]);
		System.out.println(result[2]);
	}
}
