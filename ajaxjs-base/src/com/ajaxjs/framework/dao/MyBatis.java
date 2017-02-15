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

import com.ajaxjs.Constant;
import com.ajaxjs.Init;
import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.exception.DaoException;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.jdbc.Helper;

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
	public static Configuration configuration;

	/**
	 * 
	 */
	public static SqlSessionFactory sqlSessionFactory;

	/**
	 * 使用哪一种的数据库连接
	 */
	public static String db_context_path = "jdbc/mysql_test";

	/**
	 * 初始化 MyBatis 数据链接服务
	 * 
	 * @param ds
	 */
	public static void init(final DataSource ds) {
		if (ds != null) {
			Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
			configuration = new Configuration(environment);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

			// 打印数据库连接路径，以便了解
			Connection conn = null;

			try {
				conn = environment.getDataSource().getConnection();
				System.out.println("数据库连接字符串：" + conn + Constant.ConsoleDiver);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
					}
			}
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

		System.out.println("数据库初始化成功！" + Constant.ConsoleDiver);
	}

	/**
	 * 启动 MyBatis 数据源，初始化数据库连接
	 */
	public static void init() {
		if(Init.isMac)
			db_context_path += "_mac";
			
		if(!Init.isDebug)  // 部署时读取的配置
			db_context_path += "_deploy";
		
		init(getDataSource());
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
	 * @param isUsingMySQL
	 *            是否使用 MySql
	 * @return
	 */
	public static DataSource getDataSource() {
		if (db_context_path == null)
			throw new IllegalArgumentException("参数错误，db_context_path 不能为空！");

		return Helper.getDataSource(db_context_path);
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
			// LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return neighbors;
	}
}
