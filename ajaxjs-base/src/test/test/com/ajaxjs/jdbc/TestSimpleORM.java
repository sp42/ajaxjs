package test.com.ajaxjs.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.jdbc.SimpleORM;

import test.com.ajaxjs.framework.testcase.DataSourceTestCase;
import test.com.ajaxjs.framework.testcase.News;

public class TestSimpleORM {
	Connection conn;
	
    @Before
    public void setUp() {
    	conn = DataSourceTestCase.getTestSqliteConnection();
    }
    
    @After
    public void setEnd() throws SQLException {
    	conn.close();
    }
    
	@Test
	public void testQueryMap() {
		assertNotNull(conn);
		// 返回 map
		@SuppressWarnings("rawtypes")
		SimpleORM<Map> simpleORM = new SimpleORM<>(conn, Map.class);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = simpleORM.query("SELECT * FROM news WHERE id = ?", 1);
		assertNotNull(map);
		
		// 返回 bean
		@SuppressWarnings("rawtypes")
		List<Map> list = simpleORM.queryList("SELECT * FROM news");
		assertNotNull(list);
	}
	
	@Test
	public void testQueryNews() {
		assertNotNull(conn);
		SimpleORM<News> simpleORM = new SimpleORM<>(conn, News.class);

		News news = simpleORM.query("SELECT * FROM news WHERE id = ?", 23);
		assertNotNull(news);
		
		List<News> allNews = simpleORM.queryList("SELECT * FROM news");
		assertNotNull(allNews.get(0));
	}
  
	@Test
	public void testCreateUpdateDelete(){
		News news = new News();
		news.setName("标题一");
		
		SimpleORM<News> simpleORM = new SimpleORM<>(conn, News.class);
		int newlyId = (int)simpleORM.create(news, "news");
		
		assertNotNull(newlyId);
		
		news.setName("标题二");
		news.setId(new Long(newlyId)); // 修改刚刚生成的记录
		int effectRows = (int)simpleORM.update(news, "news");
		assertNotNull(effectRows);
		
		assertTrue(simpleORM.delete(news, "news"));
	}
}
