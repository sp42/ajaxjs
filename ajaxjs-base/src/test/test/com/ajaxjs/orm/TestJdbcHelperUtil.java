package test.com.ajaxjs.orm;

import static com.ajaxjs.orm.JdbcUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.jdbc.Helper;

import test.com.ajaxjs.keyvalue.TestBeanUtil.MapMock;

public class TestJdbcHelperUtil {
	@Test
	public void testFormat() {
		assertNotNull(formatSql("SELECT * FROM news"));
		assertEquals("?,?,?", Helper.getPlaceHolder(3));
		assertEquals("birthday = ?, luckyNumbers = ?, children = ?, sex = ?, name = ?, id = ?, age = ?", Helper.getFields(MapMock.user, false));
	}

	@Test
	public void testPrintRealSql() {
		String sql = printRealSql("SELECT * FROM foo WHERE phone = ?", new Object[] { "180" });
		System.out.println(sql);
	}
}
