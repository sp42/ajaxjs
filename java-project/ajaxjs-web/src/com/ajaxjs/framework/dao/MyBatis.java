/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.ajaxjs.app.ConfigListener;
import com.ajaxjs.app.Init;
import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.exception.DaoException;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.mvc.model.BaseModel;

/**
 * 初始化数据库服务
 * 
 * @author frank
 *
 */
public class MyBatis {
	/**
	 * Configuration 需要设为 public 以便加入 mapper
	 */
	public static final Configuration configuration;

	/**
	 * 
	 */
	public static final SqlSessionFactory sqlSessionFactory;

	static {
		final DataSource ds = getDataSource();// 启动 MyBatis 数据源
		if (ds != null) {
			Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
			configuration = new Configuration(environment);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		} else {
			configuration = null;
			sqlSessionFactory = null;
		}
		// 如果要观察 MyBatis 动态生成的 SQL 语句，请打开控制台的输出。
		// org.apache.ibatis.logging.LogFactory.useStdOutLogging();
		// org.apache.ibatis.logging.LogFactory.useJdkLogging();

		// 加载通用的映射器
		if (!configuration.hasMapper(SqlProvider.class))
			configuration.addMapper(SqlProvider.class);
	}

	/**
	 * 添加一个 Mapper（DAO）。注意，返回的 SqlSession 必须手动关闭 。
	 * 
	 * @param mapperClz
	 *            该参数可以为 null。
	 * @return SqlSession
	 */
	public static SqlSession loadSession(Class<? extends DAO<? extends BaseModel>> mapperClz) {
		if (mapperClz != null && !configuration.hasMapper(mapperClz))
			configuration.addMapper(mapperClz);

		return sqlSessionFactory.openSession();
	}

	/**
	 * 创建数据源连接，这是 Web 框架运行时才能调用的方法。
	 * 
	 * @return 数据源对象
	 */
	public static DataSource getDataSource() {
		boolean isUsingMySQL = false;// 是否使用 MySql

		if (ConfigListener.config.containsKey("app_isUsingMySQL"))
			isUsingMySQL = (boolean) ConfigListener.config.get("app_isUsingMySQL");

		if (isUsingMySQL) {
			return Helper.getDataSource("jdbc/mysql_test");
		} else {
			String str;
			if (Init.isDebug)
				str = Init.isMac ? "" : "jdbc/sqlite";
			else
				str = "jdbc/sqlite_deploy";

			return Helper.getDataSource(str);
		}
	}

	/**
	 * 创建一个数据库连接对象。
	 * 
	 * @return 数据库连接对象
	 */
	public static Connection getConnection() {
		try {
			return getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Map<String, String>> getNeighbor(String tablename, long id) throws DaoException {
		List<Map<String, String>> neighbors = null;
		try {
			if (!MyBatis.configuration.hasMapper(SqlProvider.class)) {
				MyBatis.configuration.addMapper(SqlProvider.class);
			}
			
			SqlSession session = MyBatis.sqlSessionFactory.openSession();

			try {
				SqlProvider _mapper = session.getMapper(SqlProvider.class);
				neighbors = _mapper.getNeighbor(tablename, id);
			} catch (Throwable e) {
				throw e;
			} finally {
				session.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
//			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}
		
		return neighbors;
	}
}
