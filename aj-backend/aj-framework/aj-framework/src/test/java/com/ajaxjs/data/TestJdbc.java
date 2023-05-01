package com.ajaxjs.data;

import com.ajaxjs.framework.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJdbc {
    @Test
    public void testMap2InsertSql() {
        // 准备测试数据
        String tableName = "users";
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "Tom");
        map.put("age", 20);

        // 调用被测试方法
        JdbcWriter.SqlParams sp = JdbcWriter.entity2InsertSql(tableName, map);

        // 验证返回结果是否符合预期
        Assert.assertEquals("INSERT INTO users ( `name`, `id`, `age`) VALUES ( ?, ?, ?)", sp.sql);
        Assert.assertArrayEquals(new Object[]{"Tom", 1, 20}, sp.values);
    }

    @Test
    public void testMap2UpdateSql() {
        System.out.println(String.join(",", new String[2]));

        // 准备测试数据
        String tableName = "users";
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "Tom");
        map.put("age", 20);


        JdbcWriter.SqlParams sp = JdbcWriter.entity2UpdateSql(tableName, map, null, null);

        Assert.assertEquals("UPDATE users SET `name` = ?, `id` = ?, `age` = ?", sp.sql);
        Assert.assertArrayEquals(new Object[]{"Tom", 1, 20}, sp.values);

        sp = JdbcWriter.entity2UpdateSql(tableName, map, "id", 100L);

        Assert.assertEquals("UPDATE users SET `name` = ?, `id` = ?, `age` = ? WHERE id = ?", sp.sql);
        TestHelper.printArr(sp.values);
    }
}
