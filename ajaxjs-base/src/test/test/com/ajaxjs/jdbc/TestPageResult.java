package test.com.ajaxjs.jdbc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ajaxjs.jdbc.PageResult;

import test.com.ajaxjs.framework.testcase.News;

public class TestPageResult {
	@Test
	public void testPageResult() {
		News news = new News();
		news.setName("Test");

		PageResult<News> result = new PageResult<>();
//		result.addAll(new News[] { news });
		assertNotNull(result);

		assertTrue(result.size() == 1);
	}
}
