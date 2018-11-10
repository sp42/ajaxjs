package com.ajaxjs.orm.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestException {
	@Test
	public void testException() {
		DaoException e = new DaoException("");
		e.setZero(true);
		e.setOverLimit(true);
		assertNotNull(e);
	}
}
