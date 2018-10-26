package test.com.ajaxjs.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.jdbc.Helper;

import test.com.ajaxjs.framework.testcase.DataSourceTestCase;
import test.com.ajaxjs.keyvalue.TestBeanUtil.MapMock;

public class TestHelper {
	Connection conn;

	@Before
	public void setUp() throws SQLException {
		conn = DataSourceTestCase.getTestSqliteConnection();
		conn.setAutoCommit(true);
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}

	@Test
	public void testQuery() {
		assertNotNull(conn);

		Map<String, Object> info;
		info = Helper.query(conn, "SELECT * FROM news WHERE id = 1");
		assertNotNull(info);

		info = Helper.query(conn, "SELECT * FROM news WHERE id = 1 AND name like ?", "%update%");
		assertNotNull(info.get("name"));

		List<Map<String, Object>> news = Helper.queryList(conn, "SELECT * FROM news");
		assertNotNull(news.get(0).get("name"));

		int count = Helper.queryOne(conn, "SELECT COUNT(*) AS count FROM news", int.class);
		assertTrue(count > 0);
	}

	@Test
	public void testCreateUpdateDelete() throws SQLException {
		Serializable newlyId = Helper.create(conn, "INSERT INTO news (name) VALUES (?)", "test2");

		int id = (int) newlyId;
		assertNotNull(id);

		int updated;
		updated = Helper.update(conn, "UPDATE news SET name = ? WHERE id = ?", "Hi", id);
		assertEquals(1, updated);

		assertTrue(Helper.deleteById(conn, "news", id));
	}

	@Test
	public void testFormat() {
		assertNotNull(Helper.formatSql("SELECT * FROM news"));
		assertEquals("?,?,?", Helper.getPlaceHolder(3));
		assertEquals("id = ?, birthday = ?, sex = ?, age = ?, name = ?, children = ?, luckyNumbers = ?", Helper.getFields(MapMock.user, false));
	}

	@Test
	public void testPrintRealSql() {
		String sql = Helper.printRealSql("SELECT * FROM foo WHERE phone = ?", new Object[] { "180" });
		System.out.println(sql);
	}
}
