package test.com.ajaxjs.framework.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.framework.dao.DaoException;

public class TestException {
	@Test
	public void testException() {
		DaoException e = new DaoException("");
		e.setZero(true);
		e.setOverLimit(true);
		assertNotNull(e);
	}
}
