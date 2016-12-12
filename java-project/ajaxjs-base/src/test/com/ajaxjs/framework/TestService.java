package test.com.ajaxjs.framework;

import static org.junit.Assert.*;

import org.junit.*;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.PageResult;

public class TestService {
	SQLiteDataSource dataSource;

	@Before
	public void init() {
		// Construct DataSource
		try {
			SQLiteJDBCLoader.initialize();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:c:\\project\\ajaxjs\\sql\\foo.sqlite");
	}

	@Test
	public void testQuery() throws ServiceException {
		MyBatis.init(dataSource);
		NewsService newsService = new NewsService();
		System.out.println(newsService.getById(1L).getName());
		assertNotNull(newsService.getById(1L));
		
		PageResult<News> result = newsService.getPageRows(2, 2, null);
		assertTrue(result.getRows().size() == 2);
	}

	@Test
	public void testCreate() throws ServiceException {
		MyBatis.init(dataSource);
		NewsService newsService = new NewsService();
		News news = new News();
		news.setName("标题一");
		news.setService(newsService);
		int newlyId = newsService.create(news);
		System.out.println("新建记录之 id" + newlyId);
		assertNotNull(newlyId);
	}
	
	@Test
	public void testUpdate() throws ServiceException {
		MyBatis.init(dataSource);
		NewsService newsService = new NewsService();
		News news = new News();
		news.setId(1L);
		news.setName("update标题一");
		news.setService(newsService);
		boolean isOk = newsService.update(news);
		assertTrue(isOk);
	}
	
	@Test
	public void testDelete() throws ServiceException {
		MyBatis.init(dataSource);
		NewsService newsService = new NewsService();
		News news = new News();
		news.setName("TEMP");
		news.setService(newsService);
		int newlyId = newsService.create(news);
		System.out.println("newlyId:" + newlyId);
//		assertTrue(newsService.deleteByID(newlyId));
	}

}
