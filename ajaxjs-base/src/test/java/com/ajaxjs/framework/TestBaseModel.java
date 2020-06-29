package com.ajaxjs.framework;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.sql.orm.BaseModel;

public class TestBaseModel {

	@Test
	public void test() {
		assertNotNull(new BaseModel());
	}
}
