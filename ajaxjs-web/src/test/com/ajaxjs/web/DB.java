package test.com.ajaxjs.web;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;

import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.web.mock.WebBaseInit;

import test.com.ajaxjs.jdbc.TestSimpleORM;

public class DB {
	@Test
	public void testQuery() {
		String path = com.ajaxjs.util.Util.getClassFolder_FilePath(TestSimpleORM.class, "foo.sqlite");
		WebBaseInit.initDBConnection(path);
		DataSource ds =JdbcConnection.getDataSource("jdbc/sqlite");

		assertNotNull(ds);
	}
}
