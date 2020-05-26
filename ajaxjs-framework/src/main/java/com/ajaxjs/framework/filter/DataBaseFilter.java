/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework.filter;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import com.ajaxjs.Version;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.config.TestHelper;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 1、数据库连接、关闭连接；2、数据库事务
 * 
 * https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DataBaseFilter extends JdbcConnection implements FilterAction {
	private static final LogHelper LOGGER = LogHelper.getLog(DataBaseFilter.class);
	
	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		initDb();

		if (method.getAnnotation(EnableTransaction.class) != null) {
			try {
				getConnection().setAutoCommit(false);
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
		}

		return true;
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		try {
			if (args.method.getAnnotation(EnableTransaction.class) != null) {
				doTransaction(args);
			}

			if (args.method.getAnnotation(SqlAuditing.class) != null) {
				saveSql();
			}
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (TestHelper.IS_DB_CONNECTION_AUTOCLOSE) // 保证一定关闭，哪怕有异常
				closeDb();
		}

		return true;
	}

	/**
	 * 初始化数据库连接
	 */
	public static void initDb() {
		String config = ConfigService.getValueAsString("data.database_node");

		if (config == null)
			config = "jdbc/mysql"; // 如果没有 默认 mysql

		if (!Version.isDebug)
			config += "_deploy"; // 约定生产环境后面加上 _deploy

		initDbByJNDI(config);
	}

	private static void doTransaction(FilterAfterArgs argsHolder) throws SQLException {
		LOGGER.info("正在提交事务中……");
		Connection conn = getConnection();

		if (conn.isClosed())
			throw new SQLException("数据库连接已经关闭");

		if (conn.getAutoCommit())
			throw new SQLException("数据库连接没有关闭自动提交事务");

		if (argsHolder.isbeforeSkip || argsHolder.err != null)
			conn.rollback();
		else
			conn.commit();

		conn.setAutoCommit(true);
	}

	private static void saveSql() {
		for (String sql : getSqls()) {
			System.out.println(sql);
		}
	}

}
