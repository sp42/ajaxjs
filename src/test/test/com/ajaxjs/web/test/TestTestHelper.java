package test.com.ajaxjs.web.test;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;

import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.util.ClassScaner;
import com.ajaxjs.util.mock.News;
import com.ajaxjs.web.test.WebBaseInit;


public class TestTestHelper {
	@Test
	public void testQuery() {
		String path = ClassScaner.getClassFolder_FilePath(News.class, "foo.sqlite");
		WebBaseInit.initDBConnection(path);
		DataSource ds =JdbcConnection.getDataSource("jdbc/sqlite");

		assertNotNull(ds);
	}
}
