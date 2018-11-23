package com.ajaxjs.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;


public class TestQuery {
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
