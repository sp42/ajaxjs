package test.com.ajaxjs.framework;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.*;

import com.ajaxjs.framework.model.Entity;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;

public class TestModel {
	@Test
	public void testEntity() {
		Entity news = new Entity();
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
	public void testPageResult() {
		News news = new News();
		news.setName("Test");

		PageResult<News> result = new PageResult<>();
		result.setRows(new News[] { news });
		assertNotNull(result);

		assertTrue(result.getRows().size() == 1);
	}

	@Test
	public void testValid()  {
		Validator v = ModelAndView.getValidator();
		News news = new News();
		news.setName("标题");
		Set<ConstraintViolation<News>> result = v.validate(news);
		assertTrue("应该通过校验", result.isEmpty());
		
		News news2 = new News();
		result = v.validate(news2);
		assertTrue("应该不通过校验", !result.isEmpty());
		
		System.out.println(result.size());
		for(ConstraintViolation<News> r : result) {
			System.out.println(r.getMessage());
			System.out.println(r.getPropertyPath());
		}
		
	}
}
