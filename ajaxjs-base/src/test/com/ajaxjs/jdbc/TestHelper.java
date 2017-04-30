package test.com.ajaxjs.jdbc;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.util.Util;

public class TestHelper {
	Connection conn;

	@Before
	public void setUp() throws SQLException {
//		conn = JdbcConnection.getConnection("jdbc:sqlite:" + Util.getClassFolder_FilePath(TestSimpleORM.class, "foo.sqlite"));
		conn = JdbcConnection.getConnection("jdbc:mysql://192.168.61.83:7306/pms?user=root&password=2010@3gtv&useUnicode=true&characterEncoding=utf8");
		conn.setAutoCommit(true);
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}

	@Test
	public void testConnection() throws SQLException {
		assertNotNull(conn);
	}

	@Test
	public void testQuery() {
		assertNotNull(conn);

		Map<String, Object> info;
		info = Helper.query(conn, "SELECT * FROM BROADCAST_PROGRAM WHERE id = 3529166");
		assertNotNull(info);

		info = Helper.query(conn, "SELECT * FROM BROADCAST_PROGRAM WHERE id = ?", 3529166);
		System.out.println(info.get("name"));
		assertNotNull(info.get("name"));

		List<Map<String, Object>> newss = Helper.queryList(conn, "SELECT * FROM BROADCAST_PROGRAM");
		assertNotNull(newss.get(0).get("name"));
		System.out.println(newss.get(0).get("name"));
	}

//	@Test
	public void testCreateUpdateDelete() throws SQLException {
		Serializable newlyId = Helper.create(conn, "INSERT INTO news (name) VALUES (?)", "test2");
		System.out.println(newlyId);
		
		int id = (int)newlyId;
		assertNotNull(id);
		
		int updated;
		updated = Helper.update(conn, "UPDATE news SET name = ? WHERE id = ?", "Hi", id);
		assertEquals(1, updated);
		
		assertTrue(Helper.delete(conn, "news", id));
	}
}
