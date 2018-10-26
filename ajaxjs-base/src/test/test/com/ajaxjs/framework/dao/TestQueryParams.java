package test.com.ajaxjs.framework.dao;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.framework.dao.QueryParams;

public class TestQueryParams {
	Map<String, String[]> inputMap = new HashMap<>();
	
	String sql = "SELECT * FROM NEWS";

	@Test
	public void testMap() {
		inputMap.put("filterField", new String[]{"name", "age"});
		inputMap.put("filterValue", new String[]{"Jack", "18"});
		inputMap.put("searchField", new String[]{"foo", "good"});
		inputMap.put("searchValue", new String[]{"bar", "job"});
		inputMap.put("orderField", new String[]{"id", "name"});
		inputMap.put("orderValue", new String[]{"DESC", "ASC"});
		
		QueryParams qp = new QueryParams(inputMap);
		
		assertEquals("{age=18, name=Jack}", qp.filter.toString());
		assertNotNull("{good=job, foo=bar}",qp.search.toString());
		assertNotNull("{id=id, name=name}", qp.order.toString());
		assertEquals("SELECT * FROM NEWS WHERE age = 18 AND name = Jack AND good LIKE '%job%' AND foo LIKE '%bar%'", qp.addWhereToSql(sql));
		assertEquals("SELECT * FROM NEWS ORDER BY id id,name name", qp.orderToSql(sql));
	}
}
