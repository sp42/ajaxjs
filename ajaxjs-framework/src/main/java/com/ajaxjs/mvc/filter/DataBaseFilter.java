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
package com.ajaxjs.mvc.filter;

import java.lang.reflect.Method;

import com.ajaxjs.Version;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;

/**
 * 数据库连接、关闭连接
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DataBaseFilter implements FilterAction {
	/**
	 * 为方便单测，设一个开关
	 */
	public static boolean isAutoClose = true;

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method, Object[] args) {
		initDb();

		return true;
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
		if (isAutoClose)
			closeDb();
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

		JdbcConnection.initDbByJNDI(config);
	}

	/**
	 * 关闭数据库连接
	 */
	public static void closeDb() {
		JdbcConnection.closeDb();
	}
}
