package com.ajaxjs.developertools.mpb;

import com.ajaxjs.developertools.BaseTest;
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
