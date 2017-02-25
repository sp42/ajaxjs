package test.com.ajaxjs.framework.v2;

import static org.junit.Assert.*;

import org.apache.ibatis.session.SqlSession;
import org.junit.*;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.CacheService;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.MyBatis;

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
		dataSource.setUrl("jdbc:sqlite:" + com.ajaxjs.util.Util.getClassFolder_FilePath(TestService.class, "foo.sqlite"));
	}

	@Test
	public void testFindList() throws ServiceException {
		MyBatis.init(dataSource);
		NewsService newsService = new NewsService();

		try(SqlSession session = MyBatis.sqlSessionFactory.openSession();){
			newsService.setDao(session.getMapper(NewsDao.class));
			System.out.println(newsService.find(0, 99));
			assertNotNull(newsService);
		}
		
	}
}
