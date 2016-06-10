package test.com.ajaxjs.framework.dao;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.dao.SqlProvider;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class TestGenericDM {
	Connection conn;
	Configuration configuration;
	public static final String jdbcUrl = "jdbc:sqlite:c:\\project\\bigfoot\\sql\\foo.sqlite";

	@Before
	public void setUp() {
		// conn = Helper.getConnection(jdbcUrl);
		UnpooledDataSource uds = new UnpooledDataSource("org.sqlite.JDBC", jdbcUrl, "", "");
		Environment environment = new Environment("development", new JdbcTransactionFactory(), uds);
		configuration = new Configuration(environment);
	}

	@Test
	public void testConnection() {
		assertNotNull(configuration);
	}

	@Test
	public void testIsExist() {
		configuration.addMapper(SqlProvider.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		SqlSession session = sqlSessionFactory.openSession();

		SqlProvider gm = session.getMapper(SqlProvider.class);
		boolean isExist = gm.isExist("test");
		System.out.println(isExist);
		assertNotNull(isExist);
	}
}
