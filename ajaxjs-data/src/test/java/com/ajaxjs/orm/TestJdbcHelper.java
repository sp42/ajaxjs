package com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcHelper.create;
import static com.ajaxjs.orm.JdbcHelper.createBean;
import static com.ajaxjs.orm.JdbcHelper.createMap;
import static com.ajaxjs.orm.JdbcHelper.delete;
import static com.ajaxjs.orm.JdbcHelper.deleteById;
import static com.ajaxjs.orm.JdbcHelper.getBeanInfo;
import static com.ajaxjs.orm.JdbcHelper.queryAsBean;
import static com.ajaxjs.orm.JdbcHelper.queryAsBeanList;
import static com.ajaxjs.orm.JdbcHelper.queryAsMap;
import static com.ajaxjs.orm.JdbcHelper.queryAsMapList;
import static com.ajaxjs.orm.JdbcHelper.queryOne;
import static com.ajaxjs.orm.JdbcHelper.update;
import static com.ajaxjs.orm.JdbcHelper.updateBean;
import static com.ajaxjs.orm.JdbcHelper.updateMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.orm.JdbcHelper.BeanMethod;
import com.ajaxjs.orm.dao.News;

public class TestJdbcHelper {
	Connection conn;

	@Before
	public void setUp() throws SQLException {
		conn = JdbcConnection.getTestSqliteConnection();
		conn.setAutoCommit(true);
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}

	@Test
	public void testQueryAsMap() {
		Map<String, Object> info;
		info = queryAsMap(conn, "SELECT * FROM news WHERE id = 1");
		assertNotNull(info);

		info = queryAsMap(conn, "SELECT * FROM news WHERE id = ?", 1);
		assertNotNull(info);

		info = queryAsMap(conn, "SELECT * FROM news WHERE id = 1 AND name like ?", "%update%");
		assertNull(info);

		info = queryAsMap(conn, "SELECT * FROM news WHERE id = 1 AND name like ?", "%26%");
		assertNotNull(info.get("name"));
	}

	@Test
	public void testQueryAsMapList() {
		List<Map<String, Object>> news = queryAsMapList(conn, "SELECT * FROM news");
		assertNotNull(news.get(0).get("name"));
	}

	@Test
	public void testQueryOne() {
		int count = queryOne(conn, "SELECT COUNT(*) AS count FROM news", int.class);
		assertTrue(count > 0);
	}

	@Test
	public void testCreateUpdateDelete() {
		Serializable newlyId = create(conn, "INSERT INTO news (name) VALUES (?)", "test2");

		int id = (int) newlyId;
		assertNotNull(id);

		int updated;
		updated = update(conn, "UPDATE news SET name = ? WHERE id = ?", "Hi", id);
		assertEquals(1, updated);

		assertTrue(deleteById(conn, "news", id));
	}

	@Test
	public void testCreateUpdateDeleteForMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "testtest");

		Serializable newlyId = createMap(conn, map, "news");

		int id = (int) newlyId;
		assertNotNull(id);

		map.put("name", "test222222");
		int updated = updateMap(conn, map, "news");
		assertEquals(1, updated);

		assertTrue(delete(conn, map, "news"));
	}

	@Test
	public void testQueryAsBean() {
		News info;
		info = queryAsBean(News.class, conn, "SELECT * FROM news WHERE id = 1");
		assertNotNull(info);
		assertNotNull(info.getName());
	}

	@Test
	public void testQueryAsBeanList() {
		List<News> news = queryAsBeanList(News.class, conn, "SELECT * FROM news");
		assertNotNull(news.get(0).getName());
	}

	@Test
	public void testGetBeanInfo() {
		News news = new News();
		Map<String, BeanMethod> map = getBeanInfo(news);

		assertNotNull(map);
		assertNotNull(map.get("name"));

//		System.out.println(map.get("name").getGetter());
	}

	@Test
	public void testCreateUpdateDeleteForBean() {
		News news = new News();
		news.setName("test");
		Serializable newlyId = createBean(conn, news, "news");

		int id = (int) newlyId;
		assertNotNull(id);

		news.setName("test222222");
		int updated = updateBean(conn, news, "news");
		assertEquals(1, updated);

		assertTrue(delete(conn, news, "news"));
	}
}
