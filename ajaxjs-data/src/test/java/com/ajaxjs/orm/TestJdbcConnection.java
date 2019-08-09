package com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcConnection.addSql;
import static com.ajaxjs.orm.JdbcConnection.clean;
import static com.ajaxjs.orm.JdbcConnection.getConnection;
import static com.ajaxjs.orm.JdbcConnection.getDataSource;
import static com.ajaxjs.orm.JdbcConnection.getTestSqliteConnection;
import static com.ajaxjs.orm.JdbcConnection.initSqliteDBConnection;
import static com.ajaxjs.orm.JdbcConnection.setConnection;
import static com.ajaxjs.orm.JdbcConnection.testUsed_sqlite;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJdbcConnection {
	Connection conn;

	@Before
	public void setUp() throws SQLException {
		conn = getTestSqliteConnection();
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
