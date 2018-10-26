package test.com.ajaxjs.jdbc;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.jdbc.SimplePager;

import test.com.ajaxjs.framework.testcase.DataSourceTestCase;

public class TestSimplePager {
	Connection conn;

	@Before
	public void setUp() throws SQLException {
		conn = DataSourceTestCase.getTestSqliteConnection();
	}

	@After
	public void end() throws SQLException {
		conn.close();
	}

	@Test
	public void testGetResult() throws SQLException {
		SimplePager pager = new SimplePager(conn, "SELECT * FROM news", 0);
		PageResult<Map<String, Object>> result = pager.getResult(); // defaults 5
		assertNotNull(result.getTotalCount());
		assertNotNull(result.get(0).get("name"));

		pager.setPageSize(10);
	}

	@Test
	public void testGetResult2() throws SQLException {
		SimplePager pager = new SimplePager(conn, "SELECT * FROM news", "5");
		pager.setPageSize(10);

		PageResult<Map<String, Object>> result = pager.getResult(); 
		assertNotNull(result.getTotalCount());
		assertNotNull(result.get(0).get("name"));
	}
}
