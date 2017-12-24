package test.com.ajaxjs.mvc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.*;

import com.ajaxjs.framework.dao.QueryParams;

public class TestQuery {
	@Test
	public void testQuery() {
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		assertFalse(Query.isAnyMatch(request.getParameterMap()));
//
//		when(request.getParameter("filterField")).thenReturn("name");
//		when(request.getParameter("filterValue")).thenReturn("jack");
//		assertTrue(Query.isAnyMatch(request.getParameterMap()));
//
//		Map<String, String[]> mock = new HashMap<>();
//		mock.put("filterField", new String[] { "name" });
//		mock.put("filterValue", new String[] { "jack" });
//
//		when(request.getParameterMap()).thenReturn(mock);
//		QueryParams query = QueryParams.getQueryFactory(request.getParameterMap());
//
//		assertNotNull(query);
	}
}
