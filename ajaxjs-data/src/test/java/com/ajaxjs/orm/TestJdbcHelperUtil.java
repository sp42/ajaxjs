package com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcUtil.printRealSql;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.dao.NewsDao;

public class TestJdbcHelperUtil {
	NewsDao dao;

	@Before
	public void setUp() throws SQLException {
		JdbcConnection.setConnection(JdbcConnection.getTestSqliteConnection());
		dao = new Repository().bind(NewsDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	@Test
	public void testFormat() {
//		assertNotNull(formatSql("SELECT * FROM news"));
//		assertEquals("?,?,?", Helper.getPlaceHolder(3));
//		assertEquals("birthday = ?, luckyNumbers = ?, children = ?, sex = ?, name = ?, id = ?, age = ?", Helper.getFields(MapMock.user, false));
	}

	@Test
	public void testPrintRealSql() {
		String sql = printRealSql("SELECT * FROM foo WHERE phone = ?", new Object[] { "180" });
		assertNotNull(sql);
	}
}
