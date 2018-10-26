package test.com.ajaxjs.framework.service;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.*;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.jdbc.PageResult;

import test.com.ajaxjs.framework.testcase.DataSourceTestCase;
import test.com.ajaxjs.framework.testcase.News;
import test.com.ajaxjs.framework.testcase.NewsServiceImpl;

public class TestService {
	@Before
	public void init() throws SQLException {
		JdbcConnection.setConnection(DataSourceTestCase.getTestSqliteConnection());
	}

	@After
	public void clean() throws SQLException {
		JdbcConnection.getConnection().close();
	}

	@Test
	public void testQuery() throws ServiceException {
		NewsServiceImpl newsService = new NewsServiceImpl();
		assertNotNull(newsService.getFirstNews());

		PageResult<News> result = newsService.findPagedList(0, 10);
		assertTrue(result.size() == 10);
	}

	@Test
	public void testCreateAndDelete() throws ServiceException {
		NewsServiceImpl newsService = new NewsServiceImpl();
		News news = new News();
		news.setName("标题一");
		long newlyId = newsService.create(news);

		assertNotNull(newlyId);
		assertTrue(newsService.delete(news));
	}

	@Test
	public void testUpdate() throws ServiceException {
		NewsServiceImpl newsService = new NewsServiceImpl();
		News news = new News();
		news.setId(1L);
		news.setName("update标题一");
		int effectedRow = newsService.update(news);
		assertEquals(1, effectedRow);
	}
}