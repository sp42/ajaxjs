package com.ajaxjs.framework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.testcase.News;
import com.ajaxjs.framework.testcase.NewsServiceImpl;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.PageResult;

public class TestService {
	@Before
	public void init() throws SQLException {
		JdbcConnection.setConnection(JdbcConnection.getTestSqliteConnection());
	}

	@After
	public void clean() throws SQLException {
		JdbcConnection.getConnection().close();
	}

	@Test
	public void testQuery() {
		NewsServiceImpl newsService = new NewsServiceImpl();
		assertNotNull(newsService.getFirstNews());

		PageResult<News> result = newsService.findPagedList(0, 10);
		assertTrue(result.size() == 10);
	}

	@Test
	public void testCreateAndDelete() {
		NewsServiceImpl newsService = new NewsServiceImpl();
		News news = new News();
		news.setName("标题一");
		long newlyId = newsService.create(news);

		assertNotNull(newlyId);
		assertTrue(newsService.delete(news));
	}

	@Test
	public void testUpdate() {
		NewsServiceImpl newsService = new NewsServiceImpl();
		News news = new News();
		news.setId(1L);
		news.setName("update标题一");
		int effectedRow = newsService.update(news);
		assertEquals(1, effectedRow);
	}
}