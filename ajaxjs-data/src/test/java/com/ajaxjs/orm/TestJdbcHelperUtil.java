package com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcUtil.formatSql;
import static com.ajaxjs.orm.JdbcUtil.printRealSql;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestJdbcHelperUtil {
	@Test
	public void testFormat() {
		assertNotNull(formatSql("SELECT * FROM news"));
//		assertEquals("?,?,?", Helper.getPlaceHolder(3));
//		assertEquals("birthday = ?, luckyNumbers = ?, children = ?, sex = ?, name = ?, id = ?, age = ?", Helper.getFields(MapMock.user, false));
	}

	@Test
	public void testPrintRealSql() {
		String sql = printRealSql("SELECT * FROM foo WHERE phone = ?", new Object[] { "180" });
		assertNotNull(sql);
	}
}
