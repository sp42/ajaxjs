package com.ajaxjs.db_meta.tools;

import com.ajaxjs.database_meta.tools.SqlTools;
import com.ajaxjs.db_meta.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSqlTools extends BaseTest {
    @Autowired
    SqlTools sqlTools;

    @Test
    public void testRunExplainPlan(){
        SqlTools.ExplainVO explainVO = sqlTools.runExplainPlan("SELECT * FROM article LIMIT 10");

    }

    @Test
    public void testRunExplainPlanJson() {
        String s = sqlTools.runExplainPlanJson("SELECT * FROM article LIMIT 10");
        System.out.println(s);
    }
}
