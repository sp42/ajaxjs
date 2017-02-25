package test.com.ajaxjs.framework;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.util.Util;

import test.com.ajaxjs.framework.v2.NewsDao;
import test.com.ajaxjs.jdbc.TestSimpleORM;

public class TestDao {
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
	public void testDao() {
		NewsDao dao = new DaoHandler<NewsDao>().setConn(conn).bind(NewsDao.class);
		
		List<News> newsList;
		
		newsList = dao.findList(0, 2);
		System.out.println(newsList);
		assertNotNull(dao);
	}
}
