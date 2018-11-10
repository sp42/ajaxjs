package com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcConnection.*;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.util.io.resource.ScanClass;

public class TestJdbcConnection {
	Connection conn;

	/**
	 * 测试用数据库（SQLite）
	 */
	public static final String testUsed_sqlite = ScanClass.getResourcesByFileName("foo.sqlite");

	@Before
	public void setUp() throws SQLException {
		conn = getSqliteConnection(testUsed_sqlite);
	}

	@After
	public void end() throws SQLException {
		conn.close();
	}

	@Test
	public void testGetConnection() throws SQLException {
		assertNotNull(conn);
	}

	@Test
	public void testGetDataSource() throws SQLException {
		initSqliteDBConnection(testUsed_sqlite);
		assertNotNull(getDataSource("jdbc/sqlite"));
		Connection conn = getDataSource("jdbc/sqlite").getConnection();
		assertNotNull(conn);
		conn.close();
	}

	@Test
	public void testThreadLocal() {
		setConnection(conn);
		assertNotNull(getConnection());
		clean();
		addSql("SELECT * FROM news");
	}
}
