package test.com.ajaxjs.framework;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.framework.model.Entity;
import com.ajaxjs.framework.model.News;
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
}
