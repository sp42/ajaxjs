package com.ajaxjs.cms;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.app.utils.MySqlAutoBackup;
import com.ajaxjs.app.utils.MysqlExport;
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
		MySqlAutoBackup m = new MySqlAutoBackup();
		Map<String, String> map = MySqlAutoBackup.loadConfig("C:\\project\\register\\WebContent\\META-INF\\context.xml",
				"jdbc/mysql_deploy");
		assertNotNull(m);
		assertNotNull(map);
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}
}
