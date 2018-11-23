package com.ajaxjs.framework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.testcase.FakeController;
import com.ajaxjs.framework.testcase.NewsService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;

public class TestServiceIoc {
	@Before
	public void init() throws SQLException {
		JdbcConnection.setConnection(JdbcConnection.getTestSqliteConnection());
	}

	@After
	public void clean() throws SQLException {
		JdbcConnection.getConnection().close();
	}

	@Test
	public void testIoc() {
		BeanContext.init("com.ajaxjs.framework.testcase");
		BeanContext.injectBeans();
		
		NewsService newsService = (NewsService) BeanContext.getBean("newsService");

		assertNotNull(newsService);
		assertEquals("新闻", newsService.getName());
		
		FakeController controller = (FakeController) BeanContext.getBean("Controller");
		assertNotNull(controller);
		assertNotNull(controller.getService());
	}
}
