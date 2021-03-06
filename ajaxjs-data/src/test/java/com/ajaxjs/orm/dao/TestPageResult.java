package com.ajaxjs.orm.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestPageResult {
	@Test
	public void testPageResult() {
		News news = new News();
		news.setName("Test");

		PageResult<News> result = new PageResult<>();
		result.add(news);
		assertNotNull(result);

		assertTrue(result.size() == 1);
	}
}
