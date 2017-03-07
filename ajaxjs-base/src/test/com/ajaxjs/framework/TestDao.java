package test.com.ajaxjs.framework;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParam;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.util.Util;

import test.com.ajaxjs.jdbc.TestSimpleORM;

public class TestDao {
	Connection conn;
	NewsDao dao;

	@Before
	public void setUp() {
		conn = JdbcConnection.getConnection("jdbc:sqlite:" + Util.getClassFolder_FilePath(TestSimpleORM.class, "foo.sqlite"));
		dao = new DaoHandler<News, NewsDao>().setConn(conn).bind(NewsDao.class);
	}

	@After
	public void setEnd() throws SQLException {
		conn.close();
	}
	
	@Test
	public void testFindById() {
		News news = dao.findById(1L);
		System.out.println("testFindById:" + news.getName());
		assertNotNull(dao);
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
		PageResult<News> newsList = dao.findPagedList(new QueryParam(new int[]{0, 10}));
		
		assertEquals(newsList.getRows().size(), 10);
		assertNotNull(newsList);
	}
	
	@Test
	public void testTop10() {
		List<News> newsList = dao.findTop10News();
		assertEquals(newsList.size(), 10);
		assertNotNull(dao);
	}

	@Test
	public void testCreate() {
		News news = new News();
		news.setName("test 123");
		Long newlyId = dao.create(news);
		System.out.println("newlyId:" + newlyId);
		assertNotNull(newlyId);
	}
	
	@Test
	public void testUpdate() {
		 
	}
	
	@Test
	public void testDelete() {
		List<News> newsList = dao.findList(0, 5);
		assertEquals(newsList.size(), 5);
		assertNotNull(dao);
	}
}
