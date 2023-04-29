package com.ajaxjs.sql;

import org.junit.Test;

import com.ajaxjs.sql.util.SnowflakeId;

public class TestSnowflakeId {
	@Test
	public void test1() {
		System.out.println(SnowflakeId.get());
	}
}
