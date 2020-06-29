package com.ajaxjs.sql;

import static com.ajaxjs.sql.JdbcUtil.printRealSql;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.dao.NewsDao;
import com.ajaxjs.sql.orm.Repository;

public class TestJdbcHelperUtil {
	NewsDao dao;

	@Before
	public void setUp() {
		JdbcConnection.setConnection(TestJdbcConnection.getTestSqliteConnection());
		dao = new Repository().bind(NewsDao.class);
	}

	@After
	public void setEnd() {
		JdbcConnection.closeDb();
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
