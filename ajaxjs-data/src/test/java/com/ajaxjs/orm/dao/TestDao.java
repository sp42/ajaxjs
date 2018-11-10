package com.ajaxjs.orm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.mock.News;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.TestJdbcConnection;

public class TestDao {
	NewsDao dao;

	@Before
	public void setUp() {
		JdbcConnection.setConnection(JdbcConnection.getSqliteConnection(TestJdbcConnection.testUsed_sqlite));
		dao = new DaoHandler<NewsDao>().bind(NewsDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	@Test
	public void testFindById() {
		News news = dao.findById(1L);
		assertNotNull(news);
	}

	@Test
	public void testCount() {
		assertNotNull(dao.count());
	}

	@Test
	public void testFindList() {
		List<News> newsList = dao.findList(0, 5);
		assertEquals(newsList.size(), 5);
		assertNotNull(dao);
	}

	@Test
	public void testPageFindList() {
		PageResult<News> newsList = dao.findPagedList(0, 0);
		assertNotNull(newsList);
		assertEquals(10, newsList.size());
	}

	@Test
	public void testTop10() {
		List<News> newsList = dao.findTop10News();
		assertEquals(newsList.size(), 10);
		assertNotNull(dao);
	}

	@Test
	public void testCreateUpdateDelete() {
		News news = new News();
		news.setName("test 123");
		Long newlyId = dao.create(news);
		assertNotNull(newlyId);

		news.setName("test 2.");
		assertEquals(1, dao.update(news));

		assertTrue(dao.delete(news));
	}
}
