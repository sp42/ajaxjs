package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.entity.PageEnhancer;
import org.junit.Test;

public class TestSqlEnhancer {
    @Test
    public void testPage() {
        new PageEnhancer().initSql("SELECT first_name, last_name FROM employees WHERE department_id = 5 ORDER BY hire_date DESC", 0, 10);
    }
}
