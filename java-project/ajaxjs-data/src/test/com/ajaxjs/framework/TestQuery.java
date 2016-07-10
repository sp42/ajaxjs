package test.com.ajaxjs.framework;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.*;

import com.ajaxjs.framework.dao.DynamicSqlProvider;
import com.ajaxjs.framework.model.Query;

public class TestQuery {
	@Test
	public void testQuery() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		assertFalse(Query.isAnyMatch(request));

		when(request.getParameter("filterField")).thenReturn("name");
		when(request.getParameter("filterValue")).thenReturn("jack");
		assertTrue(Query.isAnyMatch(request));

		Map<String, String[]> mock = new HashMap<>();
		mock.put("filterField", new String[] { "name" });
		mock.put("filterValue", new String[] { "jack" });

		when(request.getParameterMap()).thenReturn(mock);
		Query query = Query.getQueryFactory(request);
		
		assertNotNull(query);
	}
	@Test
	public void testDynamicSqlProvider(){
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("filterField")).thenReturn("name");
		when(request.getParameter("filterValue")).thenReturn("jack");
		
		Map<String, String[]> mock = new HashMap<>();
		mock.put("filterField", new String[] { "name" });
		mock.put("filterValue", new String[] { "jack" });

		when(request.getParameterMap()).thenReturn(mock);
		Query query = Query.getQueryFactory(request);
		
		assertNotNull(query.getFilter());
		
		Map<String, Object> parames = new HashMap<>();
		parames.put("tablename", "user");
		parames.put("query", query);
		
		DynamicSqlProvider provider = new DynamicSqlProvider();
		String sql = provider.pageCount(parames);
		System.out.println(sql);
//		Query query = Query.getQueryFactory(request);
		
	}
}
