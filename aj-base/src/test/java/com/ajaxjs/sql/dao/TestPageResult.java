package com.ajaxjs.sql.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ajaxjs.sql.orm.DaoException;
import com.ajaxjs.sql.orm.PageResult;

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

	@Test
	public void testException() {
		DaoException e = new DaoException("");
		e.setZero(true);
		e.setOverLimit(true);
		assertNotNull(e);
	}
}
