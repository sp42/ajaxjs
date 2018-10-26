package test.com.ajaxjs.framework.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.jdbc.PageResult;

import test.com.ajaxjs.framework.testcase.DataSourceTestCase;
import test.com.ajaxjs.framework.testcase.News;
import test.com.ajaxjs.framework.testcase.NewsDao;

public class TestDao {
	NewsDao dao;

	@Before
	public void setUp() {
		JdbcConnection.setConnection(DataSourceTestCase.getTestSqliteConnection());
		dao = new DaoHandler<NewsDao>().bind(NewsDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		JdbcConnection.clean();
	}

	@Test
	public void testFindById() {
		News news = dao.findById(1L);
		assertNotNull(news);
	}

	@Test
	public void testCount() {
		assertNotNull(dao.count());
	}

	@Test
	public void testFindList() {
		List<News> newsList = dao.findList(0, 5);
		assertEquals(newsList.size(), 5);
		assertNotNull(dao);
	}

	@Test
	public void testPageFindList() {
		PageResult<News> pageResult = new PageResult<>();
		pageResult.setStart(0);
		pageResult.setPageSize(10);
		PageResult<News> newsList = dao.findPagedList(0, 10);

		assertEquals(newsList.size(), 10);
		assertNotNull(newsList);
	}

	@Test
	public void testTop10() {
		List<News> newsList = dao.findTop10News();
		assertEquals(newsList.size(), 10);
		assertNotNull(dao);
	}

	@Test
	public void testCreateUpdateDelete() {
		News news = new News();
		news.setName("test 123");
		Long newlyId = dao.create(news);
		assertNotNull(newlyId);

		news.setName("test 2.");
		assertEquals(1, dao.update(news));

		assertTrue(dao.delete(news));
	}
}
