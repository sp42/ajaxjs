package com.ajaxjs.orm.thirdparty;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestSnowflakeIdWorker {
	@Test
	public void test() {
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
		for (int i = 0; i < 1000; i++) {
			long id = idWorker.nextId();
			assertNotNull(id);
		}
	}
}
