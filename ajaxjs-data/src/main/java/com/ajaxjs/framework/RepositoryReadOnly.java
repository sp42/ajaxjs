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
package com.ajaxjs.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.model.ArgsInfo;
import com.ajaxjs.framework.model.PageParams;
import com.ajaxjs.orm.JdbcUtil;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.SqlFactory;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 只读的 DAO，包含分页的功能
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@SuppressWarnings("restriction")
public class RepositoryReadOnly extends RepositoryBase {
	/**
	 * 执行时的调用。不管执行哪个方法都会调用该方法。
	 * 
	 * @param proxy  代理对象
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return DAO 方法执行的结果
	 * @throws DaoException DAO 异常
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws DaoException {
		if (init(method))
			return "This is a AJAXJS DAO.";

		return select(method, args);
	}

	/**
	 * DAO 方法返回类型
	 * 
	 * @param method DAO 方法对象
	 * @return DAO 方法返回类型
	 * @throws DaoException
	 */
	Class<?> getReturnType(Method method) throws DaoException {
		Class<?> clz = method.getReturnType();

		if (clz == Map.class || clz == List.class || clz == PageResult.class || clz == int.class || clz == Integer.class
				|| clz == long.class || clz == Long.class || clz == String.class || clz == Boolean.class
				|| clz == boolean.class || clz.isArray())
			return clz;

		TableName t = getClz().getAnnotation(TableName.class);

		if (t == null && getBeanClz() == null)
			throw new DaoException("请设置注解 TableName 的 beanClass 或送入 beanClz");
		else
			clz = getBeanClz() == null ? t.beanClass() : getBeanClz();

		return clz;
	}

	/**
	 * 处理 SQL 语句
	 * 
	 * @param sql    原始 SQL 语句
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return 处理过的 SQL 语句
	 * @throws DaoException
	 */
	public ArgsInfo handleSql(String sql, Method method, Object[] args) throws DaoException {
		if (method != null && method.getAnnotation(SqlFactory.class) != null)
			sql = DaoSqlHandler.doSqlFactory(sql, method, getClz());

		if (getTableName() != null) // 替换 ${tableName}为真实的表名
			sql = sql.replaceAll("\\$\\{\\w+\\}", getTableName());

		return DaoSqlHandler.doSql(sql, method, args);
	}

	/**
	 * 执行 SELECT 查询
	 * 
	 * @param method     DAO 方法对象
	 * @param args       DAO 方法的参数
	 * @param returnType DAO 方法返回的目标类型
	 * @return 查询结果
	 * @throws DaoException
	 */
	Object select(Method method, Object[] args) throws DaoException {
		Select select = method.getAnnotation(Select.class);
		String sql = isSqlite(select.sqliteValue(), conn) ? select.sqliteValue() : select.value();

		ArgsInfo info = handleSql(sql, method, args);
		if (info.isStop)
			return null;

		sql = info.sql;
		args = info.args;

		Class<?> returnType = getReturnType(method);
		Object result = null;

		if (returnType == boolean.class || returnType == Boolean.class) {
			result = queryOne(conn, sql, Object.class, args) == null;
		} else if (returnType == int.class || returnType == Integer.class) {
			result = queryOne(conn, sql, int.class, args);
		} else if (returnType == long.class || returnType == Long.class) {
			result = queryOne(conn, sql, long.class, args);
		} else if (returnType == Integer[].class) {
			result = queryArray(conn, sql, Integer.class, args); // 不支持int[]
		} else if (returnType == String.class) {
			result = queryOne(conn, sql, String.class, args);
		} else if (returnType == List.class || returnType == PageResult.class) {
			Class<?> entryType = getEntryContainerType(method);// 实体类型的类引用，通常是 Map 或 Bean

			if (returnType == List.class)
				result = entryType == Map.class ? queryAsMapList(conn, sql, args)
						: queryAsBeanList(entryType, conn, sql, args);
			if (returnType == PageResult.class) // 分页
				result = doPage(entryType, select, info);
		} else if (returnType == Map.class) {
			result = queryAsMap(conn, sql, args);
		} else {
			result = queryAsBean(returnType, conn, sql, args); // bean
		}

		return result;
	}

	/**
	 * 获取实体的类型，可能是 map 或者 bean 类型
	 * 
	 * @param method 实体 Getter 方法
	 * @return 实体类型的类引用
	 * @throws DaoException
	 */
	private Class<?> getEntryContainerType(Method method) throws DaoException {
		Class<?> type = method.getReturnType();

		if (type.isArray())
			return type.getComponentType();

		// 获取 List<String> 泛型里的 String，而不是 List 类型
		if (type == List.class || type == PageResult.class) {
			Type returnType = method.getGenericReturnType();

			if (returnType instanceof ParameterizedType) {
				ParameterizedType _type = (ParameterizedType) returnType;

				for (Type typeArgument : _type.getActualTypeArguments()) {
					// if ("com.ajaxjs.framework.Map<String,
					// Object>".equals(typeArgument.getTypeName())) {
					// return Map<String, Object>.class;
					// }

					if ("T".equals(typeArgument.toString())) {
						TableName t = getClz().getAnnotation(TableName.class);

						if (t == null && getBeanClz() == null)
							throw new DaoException("请设置注解 TableName 的 beanClass 或送入 beanClz");
						else
							return getBeanClz() == null ? t.beanClass() : getBeanClz();
					} else if (typeArgument instanceof ParameterizedTypeImpl)
						return Map.class; // 写死的
					else
						return (Class<?>) typeArgument;
				}
			}
		}

		return type;
	}

	/**
	 * 分页操作：一、先查询有没有记录（不用查 列 column）；二、实际分页查询（加入 LIMIT ?, ? 语句，拼凑参数）
	 * 
	 * @param entityType 实体类型
	 * @param select     业务逻辑 SQL 所在的注解
	 * @param info
	 * @return 分页列表，如果找不到数据，仍返回一个空的 PageList，但可以通过 getZero() 得知是否为空
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public <B> PageResult<B> doPage(Class<B> entityType, Select select, ArgsInfo info) throws DaoException {
		PageParams p = getPageParameters(info.method, info.args);

		int total = countTotal(select, info.sql, p.args, info);

		PageResult<B> result = new PageResult<>();

		if (total <= 0) {
			LogHelper.p(info.sql + "查询完毕，没有符合条件的记录");
			result.setZero(true); // 查询完毕，没有符合条件的记录
		} else {
			int start = p.pageParams[0];
			int limit = p.pageParams[1];

			List<B> list;

			if (entityType == Map.class)
				list = (List<B>) queryAsMapList(conn, info.sql + " LIMIT ?, ?", info.args);
			else
				list = queryAsBeanList(entityType, conn, info.sql + " LIMIT ?, ?", info.args);

			result.setStart(start);
			result.setPageSize(limit);
			result.setTotalCount(total);// 先查询总数,然后执行分页
			result.page();

			if (list != null)
				result.addAll(list);
		}

		return result;
	}

	/**
	 * 获取统计行数
	 * 
	 * @param select 业务逻辑 SQL 所在的注解
	 * @param sql    业务逻辑 SQL
	 * @param args   DAO 方法参数，不要包含 start/limit 参数
	 * @param info   连接对象，判断是否 MySQL or SQLite
	 * @return 统计行数
	 * @throws DaoException
	 */
	private int countTotal(Select select, String sql, Object[] args, ArgsInfo info) throws DaoException {
		String countSql = select.countSql(), sqliteCountSql = select.sqliteCountSql();
		String _sql;

		if (CommonUtil.isEmptyString(countSql)) {
			// 另外一种统计方式，但较慢 子查询
			// 这是默认的实现，你可以通过增加 sqlCount 注解给出特定的 统计行数 之 SQL
			_sql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
			// countSql = sql.replaceAll("SELECT.*FROM", "SELECT COUNT(\\*) AS count FROM");
		} else {
			_sql = countSql;
		}

		if (isSqlite(sqliteCountSql, conn))
			_sql = sqliteCountSql;

		if (JdbcUtil.isSqlite(conn)) {
			_sql = handleSql(_sql, info.method, args).sql;
			return queryOne(conn, _sql, Integer.class, args);
		} else {// mysql 返回 long，转换一下
			_sql = handleSql(_sql, info.method, args).sql;
			Long total = queryOne(conn, _sql, Long.class, args);
			return total == null ? 0 : total.intValue();
		}
	}

	/**
	 * 获取分页参数，利用反射 DAO 方法参数列表来定位分页的 start/limit
	 * 
	 * @param method 方法对象
	 * @param args   包含分页参数 start/limit 的参数列表
	 * @return 分页信息
	 */
	private static PageParams getPageParameters(Method method, Object[] args) {
		PageParams p = new PageParams();
		int[] pageParams = new int[2];
		Parameter[] parameters = method.getParameters();

		if (CommonUtil.isNull(parameters)) {
			pageParams[0] = 0;
			pageParams[1] = PageResult.defaultPageSize;
		}

		Integer removeStartIndex = null, removeLimitIndex = null, sqlHandlerIndex = null;

		for (int i = 0; i < parameters.length; i++) {
			Parameter param = parameters[i];

			if ("arg0".equals(param.getName()) || "arg1".equals(param.getName()))
				throw new Error(
						" Java 8 支持反射获取 参数 具体名称，但要打开编译开关。例如 Eclipse 须在 Store information about method parameters (usable via reflection) 打勾，或者编译时加入参数 -parameters。");

			if (param.getName().equalsIgnoreCase("start")) {
				pageParams[0] = (int) args[i];
				removeStartIndex = i;
			} else if (param.getName().equalsIgnoreCase("limit")) {
				int l = (int) args[i];

				if (l == 0) {// limit 不能为 0
					pageParams[1] = PageResult.defaultPageSize;
					args[i] = PageResult.defaultPageSize;
				} else {
					pageParams[1] = l;
				}

				removeLimitIndex = i;
			} else if (Function.class.equals(param.getType())) {
				sqlHandlerIndex = i;
			}
		}

		List<Object> list = new ArrayList<>(); // 移除分页参数，形成新的参数列表

		for (Integer i = 0; i < args.length; i++) {
			if (i.equals(removeStartIndex) || i.equals(removeLimitIndex) || i.equals(sqlHandlerIndex)) {

			} else
				list.add(args[i]);
		}

		p.pageParams = pageParams;
		p.args = list.toArray();

		return p;
	}
}
