package com.ajaxjs.orm;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.backup.MysqlExport;

public class TestBackup {
	Connection conn;

	@Before
	public void setUp() throws SQLException {
		conn = JdbcConnection.getMySqlConnection("jdbc:mysql://", "root", "");
	}

	@Test
	public void testExport() {
		MysqlExport m = new MysqlExport(conn, "c:/temp");
		m.export();
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}
}
