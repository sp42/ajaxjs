package test.com.ajaxjs.framework.service;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.jdbc.JdbcConnection;

import test.com.ajaxjs.framework.testcase.DataSourceTestCase;
import test.com.ajaxjs.framework.testcase.FakeController;
import test.com.ajaxjs.framework.testcase.NewsService;

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
		FakeController controller = (FakeController) BeanContext.getBean("Controller");

		assertNotNull(controller.getService());
	}
}
