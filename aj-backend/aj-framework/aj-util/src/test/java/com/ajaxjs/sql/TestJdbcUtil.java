package com.ajaxjs.sql;

import org.junit.Test;

public class TestJdbcUtil {
    @Test
    public void testFormatSql() {
        String sql = "select a.id, b.name, c.age FROM t_user a JOIN t_user_profile b ON a.id = b.user_id " +
                "JOIN t_user_info c ON a.id = c.user_id WHERE a.id = 1 AND b.region_code IN (SELECT ID, NAME FROM USERS WHERE ID = 1 AND DELETED = 0 ORDER BY NAME)";
        String output = DataUtils.formatSql(sql);

        System.out.println(output);
    }
}
