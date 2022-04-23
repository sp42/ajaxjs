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

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.model.ArgsInfo;
import com.ajaxjs.sql.orm.model.PageParams;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Data Access Object 负责对数据库的增删改查工作最后的工作。 框架中一般无须写出实现，提供接口即可。 通过 Java
 * 代理自动实现接口的实例 必须是接口原类型，而不是接口其父类，也就是不支持多态
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@SuppressWarnings("restriction")
public class Repository extends RepositoryBase {
	/**
	 * 执行时的调用。不管执行哪个方法都会调用该方法。
	 *
	 * @param proxy  代理对象
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return DAO 方法执行的结果 @ DAO 异常
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		if (init(method))
			return "This is a AJAXJS DAO.";

		if (isRead(method))
			return select(method, args);

		// if (args != null && args[0] != null) {
		// Class<?> entryType = args[0].getClass();// 实体类型由参数决定，因为 写入方法通常后面跟着的就是实体
		// } else
		// throw new DaoException("DAO 接口方法:" + method + " 签名缺少实体参数！");

		if (isCreate(method))
			return insert(method, args);

		if (isUpdate(method))
			return update(method, args);

		if (isDelete(method))
			return delete(method, args);

		if (method.toString().contains("saveOrUpdate")) {
			Supplier<Serializable> fn = (Supplier<Serializable>) args[1];
			Serializable id = fn.get();

			if (id != null) { // 有已经存在的记录，是 更新
				if (args[0] instanceof Map)
					((Map<String, Object>) args[0]).put("id", Long.parseLong(id + ""));
				else
					((BaseModel) args[0]).setId(Long.parseLong(id + ""));

				return update(method, new Object[] { args[0] });
			} else
				return insert(method, new Object[] { args[0] });
		}

		throw new Error("没有任何 DAO CRUD 的注解。你继承 IDAO 接口的子接口中，可能没有覆盖 IDAO 的方法" + method);
	}

	/**
	 * 执行 SELECT 查询
	 * 
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return 查询结果 @ DAO 异常
	 */
	Object select(Method method, Object[] args) {
		Select select = method.getAnnotation(Select.class);
		String sql = JdbcConnection.getDaoContext().getDbType() == DataBaseType.SQLITE && StringUtils.hasText(select.sqliteValue()) ? select.sqliteValue() : select.value();

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
		} else if (returnType == String[].class) {
			result = queryArray(conn, sql, String.class, args); // 不支持int[]
		} else if (returnType == String.class) {
			result = queryOne(conn, sql, String.class, args);
		} else if (returnType == List.class || returnType == PageResult.class) {
			Class<?> entryType = getEntryContainerType(method);// 实体类型的类引用，通常是 Map 或 Bean

			if (returnType == List.class)
				result = entryType == Map.class ? queryAsMapList(conn, sql, args) : queryAsBeanList(entryType, conn, sql, args);
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
	 * @return 实体类型的类引用 @ DAO 异常
	 */
	private Class<?> getEntryContainerType(Method method) {
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
							throw new Error("请设置注解 TableName 的 beanClass 或送入 beanClz");
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
	 * @param <T>        实体类型
	 * @param entityType 实体类型
	 * @param select     业务逻辑 SQL 所在的注解
	 * @param info       参数信息
	 * @return 分页列表，如果找不到数据，仍返回一个空的 PageList，但可以通过 getZero() 得知是否为空 @ DAO 异常
	 */
	@SuppressWarnings("unchecked")
	public <T> PageResult<T> doPage(Class<T> entityType, Select select, ArgsInfo info) {
		PageParams p = getPageParameters(info.method, info.args);
		int total = countTotal(select, info.sql, p.args, info);
		PageResult<T> result = new PageResult<>();

		if (total <= 0) {
			LogHelper.p(info.sql + " 查询完毕，没有符合条件的记录");
			result.setZero(true); // 查询完毕，没有符合条件的记录
		} else {
			int start = p.pageParams[0];
			int limit = p.pageParams[1];

			List<T> list;

			if (entityType == Map.class)
				list = (List<T>) queryAsMapList(conn, info.sql + " LIMIT ?, ?", info.args);
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
	 * @return 统计行数 @ DAO 异常
	 */
	private int countTotal(Select select, String sql, Object[] args, ArgsInfo info) {
		if (!StringUtils.hasText(sql))
			throw new NullPointerException("SQL 语句不能为空！");

		String countSql = select.countSql(), sqliteCountSql = select.sqliteCountSql();
		String _sql;

		if (!StringUtils.hasText(countSql)) {
			// 另外一种统计方式，但较慢 子查询
			// 这是默认的实现，你可以通过增加 sqlCount 注解给出特定的 统计行数 之 SQL
			_sql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
			// countSql = sql.replaceAll("SELECT.*FROM", "SELECT COUNT(\\*) AS count FROM");
		} else
			_sql = countSql;

		switch (JdbcConnection.getDaoContext().getDbType()) {
		case MYSQL:// mysql 返回 long，转换一下
			_sql = handleSql(_sql, info.method, args).sql;
			Long total = queryOne(conn, _sql, Long.class, args);

			return total == null ? 0 : total.intValue();
		case SQLITE:
			if (StringUtils.hasText(sqliteCountSql))
				_sql = sqliteCountSql;

			_sql = handleSql(_sql, info.method, args).sql;
			return queryOne(conn, _sql, Integer.class, args);
		default:
			throw new UnsupportedOperationException("抱歉！不支持这种数据库");
		}
	}

	/**
	 * 获取分页参数，利用反射 DAO 方法参数列表来定位分页的 start/limit TODO 考虑创建cache优化
	 * 
	 * @param method 方法对象
	 * @param args   包含分页参数 start/limit 的参数列表
	 * @return 分页信息
	 */
	private static PageParams getPageParameters(Method method, Object[] args) {
		PageParams p = new PageParams();
		int[] pageParams = new int[2];
		Parameter[] parameters = method.getParameters();

		if (ObjectUtils.isEmpty(parameters)) { // 若为空的参数，采用默认分页
			pageParams[0] = 0;
			pageParams[1] = PageResult.DEFAULT_PAGE_SIZE;
		}

		// 标记特殊的分页参数和 SqlHandler，要删除，其余的则是 SQL 参数
		Integer removeStartIndex = null, removeLimitIndex = null, sqlHandlerIndex = null;

		for (int i = 0; i < parameters.length; i++) {
			Parameter _param = parameters[i];
			String param = _param.getName();

			if ("arg0".equals(param) || "arg1".equals(param))
				throw new Error(" Java 8 支持反射获取 参数 具体名称，但要打开编译开关。" + "例如 Eclipse 须在 Store information about method parameters (usable via reflection) 打勾，或者编译时加入参数 -parameters。");

			if (param.equalsIgnoreCase("start")) { // 反射获得变量名，java 8+支持
				pageParams[0] = (int) args[i];
				removeStartIndex = i;
			} else if (param.equalsIgnoreCase("limit")) {
				int l = (int) args[i];

				if (l == 0) {// limit 不能为 0
					pageParams[1] = PageResult.DEFAULT_PAGE_SIZE;
					args[i] = PageResult.DEFAULT_PAGE_SIZE;
				} else
					pageParams[1] = l;

				removeLimitIndex = i;
			} else if (Function.class.equals(_param.getType()))
				sqlHandlerIndex = i;
		}

		List<Object> sqlArgs = new ArrayList<>(); // 移除分页参数，形成新的参数列表

		for (Integer i = 0; i < args.length; i++) {
			if (i.equals(removeStartIndex) || i.equals(removeLimitIndex) || i.equals(sqlHandlerIndex)) {
			} else
				sqlArgs.add(args[i]);
		}

		p.pageParams = pageParams;
		p.args = sqlArgs.toArray(); // 纯 SQL 参数

		return p;
	}
}
