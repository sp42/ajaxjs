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
import com.ajaxjs.mvc.controller.testcase.FilterController;
import com.ajaxjs.orm.DataSourceTestCase;
import com.ajaxjs.orm.JdbcConnection;

public class TestServiceIoc {
	@Before
	public void init() throws SQLException {
		JdbcConnection.setConnection(DataSourceTestCase.getDataSource().getConnection());
	}

	@After
	public void clean() throws SQLException {
		JdbcConnection.getConnection().close();
	}

	@Test
	public void testIoc() throws ServiceException {
		BeanContext.init("com.ajaxjs.framework.mock");

		NewsService newsService = (NewsService) BeanContext.getBean("newsService");

		assertNotNull(newsService);
		assertEquals("新闻", newsService.getName());
		FilterController controller = (FakeController) BeanContext.getBean("Controller");

		assertNotNull(controller.getService());
	}
}
