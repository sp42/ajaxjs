package com.ajaxjs.app;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.app.developer.MySqExportAutoBackup;
import com.ajaxjs.app.developer.MysqlExport;
import com.ajaxjs.orm.JdbcConnection;

public class TestMySqlBackup {
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

	@Test
	public void testAutoBackup(String[] args) {
		MySqExportAutoBackup m = new MySqExportAutoBackup();
		assertNotNull(m);
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}
}
