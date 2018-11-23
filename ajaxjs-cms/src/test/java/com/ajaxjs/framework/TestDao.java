package com.ajaxjs.framework;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.testcase.NewsDao;
import com.ajaxjs.framework.testcase.NewsDao2;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

public class TestDao {
	NewsDao dao;

	@Before
	public void setUp() {
		JdbcConnection.setConnection(JdbcConnection.getTestSqliteConnection());
		dao = new DaoHandler().bind(NewsDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	Map<String, String[]> inputMap = new HashMap<>();
	{
		inputMap.put("filterField", new String[] { "status", "catelog" });
		inputMap.put("filterValue", new String[] { "2", "17" });
	}

	@Test
	public void testFindList() {
//		List<Map<String, Object>> newsList;
//		newsList = dao.findList();
//		assertEquals(214, newsList.size());

//		int total = dao.count3();
//		assertNotNull(total);
	}

//	@Test
	public void testPageFindList() {
		PageResult<News> newsList;

		newsList = dao.findPagedList(0, 10);
		assertEquals(newsList.size(), 10);

//		newsList = dao.findPagedList(new QueryParams(), 0, 10);
//		assertNotNull(newsList);
	}

	@Test
	public void test2() {
		NewsDao2 newsDao2 = new DaoHandler().bind(NewsDao2.class);
		newsDao2.findById(1L);
	}
}
