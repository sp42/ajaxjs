package com.ajaxjs.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.ajaxjs.Constant;
import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.dao.SqlProvider;

public class MyBatis {
	/**
	 * Configuration 需要设为 public 以便加入 mapper
	 */
	public static Configuration configuration;

	/**
	 * 一般用法 sqlSessionFactory.openSession()。注意，返回的 SqlSession 必须关闭
	 */
	public static SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 初始化 MyBatis 数据链接服务
	 * 
	 * @param ds
	 */
	public static void init(DataSource ds) {
		Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
		configuration = new Configuration(environment);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

		// 打印数据库连接路径，以便了解
		try (Connection conn = environment.getDataSource().getConnection();){
			System.out.println("数据库连接字符串：" + conn + Constant.ConsoleDiver);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		// 如果要观察 MyBatis 动态生成的 SQL 语句，请打开控制台的输出。
		// org.apache.ibatis.logging.LogFactory.useStdOutLogging();
		// org.apache.ibatis.logging.LogFactory.useJdkLogging();

		// 加载通用的映射器
		if (!configuration.hasMapper(SqlProvider.class))
			configuration.addMapper(SqlProvider.class);

		System.out.println("数据库初始化成功！" + Constant.ConsoleDiver);
	}
	
	/**
	 * 添加一个 Mapper（DAO）
	 * @param clz
	 */
	public static void addDao(Class<? extends BaseDao<?, ?>> clz){
		if (clz != null && !configuration.hasMapper(clz))
			configuration.addMapper(clz);
	}
}
