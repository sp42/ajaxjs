/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.jdbc.SimpleORM;
import com.ajaxjs.util.CollectionUtil;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.NewInstance;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Data Access Object 负责对数据库的增删改查工作最后的工作。 框架中一般无须写出实现，提供接口即可。 通过 Java
 * 代理自动实现接口的实例 必须是接口原类型，而不是接口其父类，也就是不支持多态
 * 
 * @author Sp42 frank@ajaxjs.com
 * @param <T> DAO 实际类型引用
 */
public class DaoHandler<T> implements InvocationHandler {
	private static final LogHelper LOGGER = LogHelper.getLog(DaoHandler.class);

	/**
	 * 数据库连接对象。You should put connection by calling
	 * JdbcConnection.setConnection(conn).
	 */
	private Connection conn;

	/**
	 * 获取数据库连接对象
	 * 
	 * @return 数据库连接对象
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * 设置数据库连接对象
	 * 
	 * @param conn 数据库连接对象
	 * @return DAO
	 */
	public DaoHandler<T> setConn(Connection conn) {
		this.conn = conn;
		return this;
	}

	/**
	 * 执行时的调用。不管执行哪个方法都会调用该方法。
	 * 
	 * @throws DaoException DAO 异常
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws DaoException {
		if (method.getName().equals("toString")) // 没有默认的 toString() 这里实现一个
			return "This is a AJAXJS DAO.";

		setConn(JdbcConnection.getConnection()); // 从线程中获数据库连接对象

		if (getConn() == null)
			throw new DaoException("没有 connection， 请先建立数据库连接对象。"); // 再检查

		Class<?> returnType = method.getReturnType(), entryType = null; // DAO 分法返回的类型；实体类型

		if (method.getAnnotation(Select.class) != null) {
			entryType = getEntryContainerType(method);
			return select(method.getAnnotation(Select.class), args, returnType, entryType, method);
		}

		if (args != null && args[0] != null)
			entryType = args[0].getClass();// 实体类型由参数决定
		else
			throw new DaoException("DAO 接口方法:" + method + " 签名缺少实体参数！");

		if (method.getAnnotation(Insert.class) != null)
			return insert(method.getAnnotation(Insert.class), args, returnType, entryType);

		if (method.getAnnotation(Update.class) != null)
			return update(method.getAnnotation(Update.class), args, entryType);

		if (method.getAnnotation(Delete.class) != null)
			return delete(method.getAnnotation(Delete.class), args, entryType);

		throw new DaoException("没有任何 DAO CRUD 的注解。你继承 IDAO 接口的子接口中，可能没有覆盖 IDAO 的方法" + method);
	}

	/**
	 * 绑定 DAO 的类，实例化该接口，返回实例
	 * 
	 * @param clz DAO 实际类引用，必须为接口
	 * @return DAO 实例
	 */
	@SuppressWarnings("unchecked")
	public T bind(Class<T> clz) {
		Object obj = Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, this);
		return (T) obj;
	}

	/**
	 * 获取实体的类型，可能是 map 或者 bean 类型
	 * 
	 * @param method 实体 Getter 方法
	 * @return 实体类型的类引用
	 */
	private static Class<?> getEntryContainerType(Method method) {
		Class<?> type = method.getReturnType();

		// 获取 List<String> 泛型里的 String，而不是 List 类型
		if (type == List.class || type == PageResult.class) {
			Type returnType = method.getGenericReturnType();

			if (returnType instanceof ParameterizedType) {
				ParameterizedType _type = (ParameterizedType) returnType;

				for (Type typeArgument : _type.getActualTypeArguments()) {
					if (typeArgument instanceof ParameterizedTypeImpl) {
						return Map.class; // 写死的
					} else
						return (Class<?>) typeArgument;
				}
			}
		}

		return type;
	}

	/**
	 * 执行 SELECT 查询
	 * 
	 * @param method DAO 方法
	 * @param args 参数
	 * @param returnType DAO 方法返回的目标类型
	 * @param entryType 实体类型的类引用，通常是 map 或 bean
	 * @return 查询结果
	 * @throws DaoException
	 */
	private <R, B> Object select(Select select, Object[] args, Class<R> returnType, Class<B> entryType, Method method) throws DaoException {
		String sql = isSqlite(select.sqliteValue(), conn) ? select.sqliteValue() : select.value();

		SqlAndArgs s = new SqlAndArgs();
		s.sql = sql;
		s.args = args;

		s = doSqlFactory(select, s);
		s = criteria.toSql(s);

		args = s.args;
		sql = s.sql;

		SimpleORM<B> orm = new SimpleORM<>(conn, entryType);

		if (returnType == int.class) {
			return Helper.queryOne(conn, sql, int.class, args);
		} else if (returnType == Integer[].class) {
			return Helper.queryArray(conn, sql, Integer.class, args); // 不支持int[]
		} else if (returnType == String.class) {
			return Helper.queryOne(conn, sql, String.class, args);
		} else if (returnType == List.class) {
			return orm.queryList(sql, args);
		} else if (returnType == PageResult.class) {// 分页
			int total = pager.countTotal(select, sql, args, conn);

			PageResult<B> result = new PageResult<>();

			if (total <= 0) {
				LOGGER.info("查询完毕，没有符合条件的记录");
				result.setZero(true);
			} else {
				s = pager.toSql(s);

				int start = (int) args[args.length - 2];
				int limit = (int) args[args.length - 1];

				if (limit == 0) {
					args[args.length - 1] = limit = 8;
				}

				List<B> list = orm.queryList(s.sql, s.args);
				result.setStart(start);
				result.setPageSize(limit);
				result.setTotalCount(total);// 先查询总数,然后执行分页
				result.page();

				if (list != null)
					result.addAll(list);
			}

//				queryParam.order = new HashMap<>(); // 默认按照 id 排序
//				queryParam.order.put("id", "DESC");
//				sql = queryParam.orderToSql(sql);
			return result;
		} else {
			return orm.query(sql, args);// maybe a bean
		}
	}

	/**
	 * 
	 * @param select
	 * @param s
	 * @return
	 */
	private SqlAndArgs doSqlFactory(Select select, SqlAndArgs s) {
		Class<? extends SqlFactory>[] fs = select.sqlFactory();

		if (!CollectionUtil.isNull(fs)) {
			for (Class<? extends SqlFactory> f : fs) {
				SqlFactory instance = NewInstance.newInstance(f);
				s = instance.toSql(s);
			}
		}

		return s;
	}

	/**
	 * 
	 */
	public final static SqlFactoryCriteria criteria = new SqlFactoryCriteria();

	/**
	 * 
	 */
	private final static SqlFactoryPager pager = new SqlFactoryPager();

	/**
	 * 判断是否 SQLite 数据库
	 * 
	 * @param sqliteValue SQLite 数据库专用的 SQL 语句
	 * @param conn 数据库连接对象
	 * @return true = 是 SQLite 数据库
	 */
	public static boolean isSqlite(String sqliteValue, Connection conn) {
		return !StringUtil.isEmptyString(sqliteValue) && conn.toString().contains("sqlite");
	}

	/**
	 * 新增动作
	 * 
	 * @param insert 包含 SQL 的注解
	 * @param args SQL 参数
	 * @param returnType DAO 方法返回的目标类型
	 * @param beanType 实体类型的类引用，通常是 map 或 bean
	 * @return 自增 id
	 */
	@SuppressWarnings("unchecked")
	private <R, B> Serializable insert(Insert insert, Object[] args, Class<R> returnType, Class<B> beanType) {
		Serializable id = null; // INSERT 返回新建的 id

		if (!insert.value().equals("")) { /* 以 sql 方式创建 */
			id = Helper.create(conn, insert.value(), args);
		} else if (insert.value().equals("") && insert.tableName() != null && args[0] != null) {// 以 bean 方式创建
			id = new SimpleORM<>(conn, beanType).create((B) args[0], insert.tableName());
		}

		if (returnType == Integer.class && id.getClass() == Long.class) {
			return Integer.parseInt("" + id);
		} else if (returnType == Long.class && id.getClass() == Integer.class) {
			return new Long((Integer) id);
		} else {
			return id;
		}
	}

	/**
	 * 更新动作
	 * 
	 * @param update 包含 SQL 的注解
	 * @param args SQL 参数
	 * @param beanType 实体类型的类引用，通常是 map 或 bean
	 * @return 影响的行数
	 */
	@SuppressWarnings("unchecked")
	private <B> Integer update(Update update, Object[] args, Class<B> beanType) {
		int effectRows = 0;

		if (!update.value().equals("")) { /* 以 sql 方式更新 */
			effectRows = Helper.update(conn, update.value(), args);
		} else if (update.value().equals("") && update.tableName() != null && args[0] != null) { // 以 bean 方式删除
			effectRows = new SimpleORM<>(conn, beanType).update((B) args[0], update.tableName());
		}

		return effectRows;
	}

	/**
	 * 删除动作
	 * 
	 * @param delete 包含 SQL 的注解
	 * @param args SQL 参数
	 * @param beanType 实体类型的类引用，通常是 map 或 bean
	 * @return 是否删除成功
	 */
	@SuppressWarnings("unchecked")
	private <B> Boolean delete(Delete delete, Object[] args, Class<B> beanType) {
		boolean isOk = false;

		String sql = isSqlite(delete.sqliteValue(), conn) ? delete.sqliteValue() : delete.value();

		if (!StringUtil.isEmptyString(sql)) { /* 以 sql 方式删除 */
			isOk = Helper.delete(conn, sql, args);
		} else if (delete.value().equals("") && delete.tableName() != null && args[0] != null) { // 以 bean 方式删除
			isOk = new SimpleORM<>(conn, beanType).delete((B) args[0], delete.tableName());
		}

		return isOk;
	}
}
