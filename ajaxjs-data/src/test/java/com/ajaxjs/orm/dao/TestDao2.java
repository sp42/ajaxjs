package com.ajaxjs.orm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.JdbcConnection;

public class TestDao2 {
	NewsDao2 dao;
	NewsDaoMap2 mapDao;

	@Before
	public void setUp() {
		JdbcConnection.setConnection(JdbcConnection.getTestSqliteConnection());
		dao = new Repository().bind(NewsDao2.class);
		mapDao = new Repository().bind(NewsDaoMap2.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	@Test
	public void testFindById() {
		News news = dao.findById(1L);
		assertNotNull(news);
		Map<String, Object> newsMap = mapDao.findById(1L);
		assertNotNull(newsMap);
		assertNotNull(newsMap.get("name"));
	}

	@Test
	public void testCount() {
		assertNotNull(dao.count());
		assertNotNull(mapDao.count());
	}

	@Test
	public void testFindList() {
		List<News> newsList = dao.findList(0, 5);
		assertNotNull(newsList);
		assertEquals(5, newsList.size());

		List<EntityMap> newsListMap = mapDao.findList(0, 5);
		assertNotNull(newsListMap);
		assertEquals(5, newsListMap.size());
	}

	@Test
	public void testPageFindList() {
		PageResult<News> newsList = dao.findPagedList(0, 0);
		assertNotNull(newsList);
		assertEquals(10, newsList.size());

		PageResult<EntityMap> newsPagedListMap = mapDao.findPagedList(0, 5);
		assertNotNull(newsPagedListMap);
		assertEquals(5, newsPagedListMap.size());
	}

	@Test
	public void testTop10() {
		List<News> newsList = dao.findTop10News();
		assertNotNull(newsList);
		assertEquals(10, newsList.size());

		List<EntityMap> newsListMap = mapDao.findTop10News();
		assertNotNull(newsListMap);
		assertEquals(10, newsListMap.size());
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

		EntityMap newsMap = new EntityMap();
		newsMap.put("name", "test 123");
		newlyId = mapDao.create(newsMap);
		assertNotNull(newlyId);

		newsMap.put("name", "test 2.");
		assertEquals(1, mapDao.update(newsMap));

		assertTrue(mapDao.delete(newsMap));
	}

	@Test
	public void testSqlFactory() {
		int total = dao.count2();
		assertNotNull(total); 
	}

}
