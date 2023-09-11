package com.ajaxjs.data;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDerby {
    @Test
    public void test() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        // add application code here
        conn.close();

    }
}
