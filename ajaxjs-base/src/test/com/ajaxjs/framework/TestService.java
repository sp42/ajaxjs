package test.com.ajaxjs.framework;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.*;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.ConnectionMgr;

import test.com.ajaxjs.jdbc.TestSimpleORM;

public class TestService {
	SQLiteDataSource dataSource;

	@Before
	public void init() throws SQLException {
		// Construct DataSource
		try {
			SQLiteJDBCLoader.initialize();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:" + com.ajaxjs.util.Util.getClassFolder_FilePath(TestSimpleORM.class, "foo.sqlite"));
	
		ConnectionMgr.setConnection(dataSource.getConnection());
	}

	@After
	public void clean() throws SQLException{
		ConnectionMgr.getConnection().close();
	}
	
	@Test
	public void testQuery() throws ServiceException {
		NewsService newsService = new NewsService();
		System.out.println("testQuery:" + newsService.getFirstNews());
		assertNotNull(newsService.getFirstNews());

//		PageResult<News> result = newsService.getPageRows(2, 2, null);
//		assertTrue(result.getRows().size() == 2);
	}
//
//	@Test
//	public void testQueryCache() throws ServiceException {
//
//	 
//			IService<News> newsService = new NewsService();
//			newsService = new CacheService<News>().bind(newsService);
//			//newsService = (NewsService)new CacheService().bind(new NewsService());
//			System.out.println("::::::" + newsService.getPageRows(2, 2, null));
//			//Object obj = new CacheService().bind(new NewsService()).getTableName();
//		 
//		//IService newsService2 =new CacheService().bind(newsService);
//		//PageResult result = newsService2.getPageRows(2, 2, null);
//	}
//
//	//@Test
//	public void testCreate() throws ServiceException {
//		NewsService newsService = new NewsService();
//		News news = new News();
//		news.setName("标题一");
//		news.setService(newsService);
//		int newlyId = newsService.create(news);
//		System.out.println("新建记录之 id" + newlyId);
//		assertNotNull(newlyId);
//	}
//
//	// @Test
//	public void testUpdate() throws ServiceException {
//		NewsService newsService = new NewsService();
//		News news = new News();
//		news.setId(1L);
//		news.setName("update标题一");
//		news.setService(newsService);
//		boolean isOk = newsService.update(news);
//		assertTrue(isOk);
//	}
//
//	// @Test
//	public void testDelete() throws ServiceException {
//		NewsService newsService = new NewsService();
//		News news = new News();
//		news.setName("TEMP");
//		news.setService(newsService);
//		int newlyId = newsService.create(news);
//		System.out.println("newlyId:" + newlyId);
//		// assertTrue(newsService.deleteByID(newlyId));
//	}

}
