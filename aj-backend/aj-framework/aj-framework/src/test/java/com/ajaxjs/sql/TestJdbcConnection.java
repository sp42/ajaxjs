package com.ajaxjs.sql;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static com.ajaxjs.sql.JdbcConnection.getMySqlConnection;
import static org.junit.Assert.assertNotNull;

public class TestJdbcConnection {
    @Test
    public void testGetMySqlConnection() throws SQLException {
        Connection conn = getMySqlConnection("jdbc:mysql://xxx:yyy/test?useUnicode=true", "root", "xxxxx");
        assertNotNull(conn);
        conn.close();
    }
}
