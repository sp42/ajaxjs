package test.com.ajaxjs.framework.model;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.*;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.service.aop.ValidationService;

import test.com.ajaxjs.framework.testcase.News;

public class TestBaseModel {
	@Test
	public void testEntity() {
		BaseModel news = new BaseModel();
		assertNotNull(news);
		news.setName("Test");
		assertNotNull(news.getName());
	}

	@Test
	public void testNewsModel() {
		News news = new News();
		assertNotNull(news);
		news.setName("Test");
		assertNotNull(news.getName());
	}

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
