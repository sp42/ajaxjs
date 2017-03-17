package test.com.ajaxjs.framework.service;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.framework.service.ValidationService;
import com.ajaxjs.jdbc.ConnectionMgr;

import test.com.ajaxjs.framework.News;
import test.com.ajaxjs.framework.NewsDao;
import test.com.ajaxjs.framework.NewsService;
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
	public void testValid() throws ServiceException {
		IService<News, Long> service = new ValidationService<News, Long, NewsDao>().bind(new NewsService());
		News news = service.findById(1L);
		System.out.println(news.getName());
		news.setName(null);
		service.create(news);
		 
	}
}
