package test.com.ajaxjs.jdbc;

import static org.junit.Assert.*;

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
    public void setUp() {
    	conn = JdbcConnection.getConnection("jdbc:sqlite:" + Util.getClassFolder_FilePath(TestSimpleORM.class, "foo.sqlite"));
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
	public void testQuery(){
		assertNotNull(conn);
		
		Map<String, Object> info;
		info = Helper.query(conn, "SELECT * FROM news WHERE id = 1");
		// System.out.println(info);
		assertNotNull(info);
		
		info = Helper.query(conn, "SELECT * FROM news WHERE id = ?", 1);
		System.out.println(info.get("name"));
		assertNotNull(info.get("name"));
		
		List<Map<String, Object>> newss = Helper.queryList(conn, "SELECT * FROM news");
		assertNotNull(newss.get(0).get("name"));
		System.out.println(newss.get(0).get("name"));
	}

}
