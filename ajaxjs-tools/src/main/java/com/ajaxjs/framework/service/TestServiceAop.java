package com.ajaxjs.framework.service;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.testcase.News;
import com.ajaxjs.framework.testcase.NewsService;
import com.ajaxjs.framework.testcase.NewsServiceImpl;
import com.ajaxjs.orm.JdbcConnection;

public class TestServiceAop {
	@Before
	public void init() throws SQLException {
		JdbcConnection.setConnection(JdbcConnection.getTestSqliteConnection());
	}

	@After
	public void clean() throws SQLException {
		JdbcConnection.getConnection().close();
	}

	@Test
	public void testQueryCache() {
		NewsServiceImpl newsServiceImpl = new NewsServiceImpl();
//		NewsService newsService = new CacheService<News, Long, NewsService>().bind(newsServiceImpl);

//		assertEquals(2, newsService.findPagedList(2, 2).size());
//		newsService = new CommonTestService<News, Long, NewsService>().bind(newsService);
//		News news = new News();
//		news.setName("test");
//		Long id = newsService.create(news);

//		assertNotNull(id);
//		assertEquals(id, news.getId());
	}

	@Test
	public void testAopLink() {
		NewsServiceImpl im = new NewsServiceImpl();
//		NewsService service = Aop.chain(im, new CommonTestService<News, Long, NewsService>(), new CacheService<News, Long, NewsService>());
//		assertEquals(2, service.findPagedList(2, 2).size());
	}

	@Test
	public void testValid() {
		NewsService service = new ValidationService<News, Long, NewsService>().bind(new NewsServiceImpl());
		News news = service.findById(1L);
		news.setName(null);

		service.create(news);
	}
}
