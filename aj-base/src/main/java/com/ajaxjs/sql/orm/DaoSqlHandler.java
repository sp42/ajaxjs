/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.sql.orm;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ajaxjs.sql.annotation.SqlFactory;
import com.ajaxjs.sql.orm.model.ArgsInfo;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;

/**
 * SQL语句可以得到再处理的，有这个类的两种方法
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DaoSqlHandler {
	/**
	 * 找到 SqlHandler（函数式版）并将其从参数列表中剔除，拿出来执行，返回新的 SQL。因为剔除了，这是 args 亦有变动，剩下的才是SQL参数
	 * 
	 * @param sql    SQL 语句
	 * @param method DAO 方法对象
	 * @param args   DAO 执行时实际传入的参数，参数有变动
	 * @return 处理过的参数信息
	 */
	static ArgsInfo doSql(String sql, Method method, Object[] args) {
		ArgsInfo info = new ArgsInfo();
		info.sql = sql;
		info.args = args;
		info.method = method;

		Parameter[] parameters = method.getParameters();

		if (!CommonUtil.isNull(args) && !CommonUtil.isNull(parameters)) {
			String realSql = null;
			List<Object> list = new ArrayList<>();
			boolean found = false;

			for (int i = 0; i < args.length; i++) {
				Object obj = args[i];
				Parameter p = parameters[i];

				if (Function.class.equals(p.getType())) {
					found = true;

					if (obj instanceof Function) {
						@SuppressWarnings("unchecked")
						Function<String, String> fn = (Function<String, String>) obj;
						String _sql = fn.apply(sql);

						if (_sql == null)
							info.isStop = true;

						realSql = _sql == null ? sql : _sql;
						info.sqlHandler = fn;
					} else {
						// obj is null; 虽然接口上有声明，但实际没有传入 Function<String, String>，依然是原来的 SQL 不变
						realSql = sql; // SQL 不变，去掉 fn 参数
						info.sqlHandler = null;
					}
				} else
					list.add(obj);
			}

			if (found) {
				info.sql = realSql;
				info.args = list.toArray();
			}
		}

		return info;
	}

	/**
	 * 基于注解的 SQL 处理器
	 * 
	 * @param sql    SQL 语句
	 * @param method DAO 方法对象
	 * @param daoClz DAO 类引用
	 * @return 处理过的 SQL
	 * @throws DaoException
	 */
	static String doSqlFactory(String sql, Method method, Class<?> daoClz) throws DaoException {
		SqlFactory sqlFactory = method.getAnnotation(SqlFactory.class);
		String methodName = sqlFactory.value();
		Class<?> clz = sqlFactory.clz();

		if (clz == null || clz.equals(Object.class))
			clz = daoClz;

		Method sqlFactoryHandler = ReflectUtil.getMethod(clz, methodName, String.class);

		if (sqlFactoryHandler == null)
			throw new DaoException("找不到对应的 SqlFactory，应该为 Function<String, String>");

		sql = ReflectUtil.executeStaticMethod(sqlFactoryHandler, sql).toString();

		return sql;
	}
}
