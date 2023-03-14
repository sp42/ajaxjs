package com.ajaxjs.framework.sql;

import org.junit.Test;

public class TestSqlEnhancer {

	@Test
	public void test() {
		SqleEnhancer.page("SELECT first_name, last_name FROM employees WHERE department_id = 5 ORDER BY hire_date DESC", 0, 10);
	}
}
