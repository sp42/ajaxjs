package com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcConnection.*;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ajaxjs.orm.testcase.DataSourceTestCase;

public class TestJdbcConnection {
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
	public void testGetConnection() throws SQLException {
		assertNotNull(conn);

		Connection conn2 = getConnection("jdbc:sqlite:C:\\sp42\\project\\ajaxjs-data\\src\\main\\com\\ajaxjs\\framework\\mock\\foo.sqlite");
		assertNotNull(conn2);
		conn2.close();
	}

	@Test
	public void testGetDataSource() throws SQLException {
		initSqliteDBConnection(DataSourceTestCase.testUsed_sqlite);
		assertNotNull(getDataSource("jdbc/sqlite"));
	}

	@Test
	public void testConnectionFromDataSource() throws SQLException {
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
