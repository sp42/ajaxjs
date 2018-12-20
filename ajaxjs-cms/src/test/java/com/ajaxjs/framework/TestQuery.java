package com.ajaxjs.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.testcase.NewsDao;
import com.ajaxjs.framework.testcase.NewsDao2;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;


public class TestQuery {
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

	Map<String, String[]> inputMap2 = new HashMap<>();
	{
		inputMap2.put("filterField", new String[] { "status", "catelog" });
		inputMap2.put("filterValue", new String[] { "2", "17" });
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
	
	@Test
	public void testQuery() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("filterField")).thenReturn("name");
		when(request.getParameter("filterValue")).thenReturn("jack");

		Map<String, String[]> mock = new HashMap<>();
		mock.put("filterField", new String[] { "name" });
		mock.put("filterValue", new String[] { "jack" });

		when(request.getParameterMap()).thenReturn(mock);

		QueryParams q = new QueryParams(request.getParameterMap());

		assertEquals(" WHERE name = jack", q.addWhereToSql(""));
	}

	Map<String, String[]> inputMap = new HashMap<>();

	String sql = "SELECT * FROM NEWS";

	@Test
	public void testMap() {
		inputMap.put("filterField", new String[] { "name", "age" });
		inputMap.put("filterValue", new String[] { "Jack", "18" });
		inputMap.put("searchField", new String[] { "foo", "good" });
		inputMap.put("searchValue", new String[] { "bar", "job" });
		inputMap.put("orderField", new String[] { "id", "name" });
		inputMap.put("orderValue", new String[] { "DESC", "ASC" });

		QueryParams qp = new QueryParams(inputMap);

		assertEquals("{age=18, name=Jack}", qp.filter.toString());
		assertNotNull("{good=job, foo=bar}", qp.search.toString());
		assertNotNull("{id=id, name=name}", qp.order.toString());
		assertEquals("SELECT * FROM NEWS WHERE age = 18 AND name = Jack AND good LIKE '%job%' AND foo LIKE '%bar%'", qp.addWhereToSql(sql));
		assertEquals("SELECT * FROM NEWS ORDER BY id id,name name", qp.orderToSql(sql));
	}
}
