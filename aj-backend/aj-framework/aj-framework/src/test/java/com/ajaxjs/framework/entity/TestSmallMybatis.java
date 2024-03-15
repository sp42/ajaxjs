package com.ajaxjs.framework.entity;

import com.ajaxjs.data.SmallMyBatis;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestSmallMybatis {
    SmallMyBatis xmlSqlHelper = new SmallMyBatis();

    @Test
    public void loadXML() {
        xmlSqlHelper.loadXML();
    }

    @Test
    public void loadIf() {
        String sql = "SELECT * FROM user WHERE name LIKE #{name} AND " +
                "<if test=\"age eq 20\">age = ${age}</if><if test=\"age ne 20\">" +
                "#{T(com.ajaxjs.framework.entity.TestSmallMybatis).p()}</if>";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "%Tom%");
        paramMap.put("age", "240");

        String actualSql = SmallMyBatis.generateIfBlock(sql, paramMap);

        actualSql = SmallMyBatis.getValuedSQL(actualSql, paramMap);
        System.out.println(actualSql);
    }
}
