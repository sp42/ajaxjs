package test.com.ajaxjs.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.jdbc.SimpleORM;
import com.ajaxjs.util.Util;

import test.com.ajaxjs.framework.News;


public class TestSimpleORM {
	Connection conn;
	
    @Before
    public void setUp() {
    	conn = JdbcConnection.getConnection("jdbc:sqlite:" + Util.getClassFolder_FilePath(TestSimpleORM.class, "foo.sqlite"));
    }
    
    @After
    public void setEnd() throws SQLException {
    	conn.close();
    }
    
	@Test
	public void testQueryMap() {
		assertNotNull(conn);
		
		@SuppressWarnings("rawtypes")
		SimpleORM<Map> simpleORM = new SimpleORM<>(conn, Map.class);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = simpleORM.query("SELECT * FROM news WHERE id = ?", 1);
		assertNotNull(map);
		System.out.println(map.get("name"));
		
		@SuppressWarnings("rawtypes")
		List<Map> list = simpleORM.queryList("SELECT * FROM news");
		assertNotNull(list);
		System.out.println(list.get(0).get("name"));
	}
	
	@Test
	public void testQueryNews() {
		assertNotNull(conn);
		SimpleORM<News> simpleORM = new SimpleORM<>(conn, News.class);

		News news = simpleORM.query("SELECT * FROM news WHERE id = ?", 23);
		assertNotNull(news);
		System.out.println(news.getName());
		
		List<News> allNews = simpleORM.queryList("SELECT * FROM news");
		assertNotNull(allNews);
		System.out.println(allNews.get(23).getName());
	}
  
	@Test
	public void testCreate(){
		News news = new News();
		news.setName("标题一");
		
		SimpleORM<News> simpleORM = new SimpleORM<>(conn, News.class);
		long newlyId = (long)simpleORM.create(news, "news");
		
		assertNotNull(newlyId);
		System.out.println(newlyId);
	}
}
