package com.ajaxjs.sql;

import static com.ajaxjs.sql.JdbcConnection.addSql;
import static com.ajaxjs.sql.JdbcConnection.getConnection;
import static com.ajaxjs.sql.JdbcConnection.getMySqlConnection;
import static com.ajaxjs.sql.JdbcConnection.setConnection;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.ajaxjs.util.ioc.EveryClass;

public class TestJdbcConnection {
	/**
	 * 创建 SQLite 数据库连接对象（测试用）
	 *
	 * @return 数据库连接对象
	 */
	public static Connection getTestSqliteConnection() {
		return JdbcConnection.getSqliteConnection(EveryClass.getResourcesFromClasspath("test_used_database.sqlite"));
	}

	@Test
	public void testGetSqliteConnection() throws SQLException {
		Connection conn = getTestSqliteConnection();
		assertNotNull(conn);
		conn.close();
	}

//	@Test
	public void testGetMySqlConnection() throws SQLException {
		Connection conn = getMySqlConnection("jdbc:mysql://xxx:yyy/test?useUnicode=true", "root", "xxxxx");
		assertNotNull(conn);
		conn.close();
	}

//	@Test
	public void testThreadLocal() {
		Connection conn = getTestSqliteConnection();
		setConnection(conn);
		assertNotNull(getConnection());
		JdbcConnection.closeDb();
		addSql("SELECT * FROM news");
	}
}
