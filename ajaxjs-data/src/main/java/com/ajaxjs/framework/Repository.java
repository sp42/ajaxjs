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
package com.ajaxjs.framework;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.SqlFactory;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Data Access Object 负责对数据库的增删改查工作最后的工作。 框架中一般无须写出实现，提供接口即可。 通过 Java
 * 代理自动实现接口的实例 必须是接口原类型，而不是接口其父类，也就是不支持多态
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class Repository extends JdbcHelper implements InvocationHandler {

	/**
	 * 数据库连接对象。You should put connection by calling JdbcConnection.setConnection(conn).
	 */
	private Connection conn;

	/**
	 * 执行时的调用。不管执行哪个方法都会调用该方法。
	 * 
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws DaoException DAO 异常
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws DaoException {
		if (method.getName().equals("toString")) // 没有默认的 toString() 这里实现一个
			return "This is a AJAXJS DAO.";

		conn = JdbcConnection.getConnection(); // 从线程中获数据库连接对象

		if (conn == null)
			throw new DaoException("没有 connection， 请先建立数据库连接对象。"); // 再检查

		Class<?> returnType = getReturnType(method), entryType = null; // DAO 分法返回的类型；实体类型

		Method sqlFactoryHandler = getSqlFactoryHandler(method);

		if (method.getAnnotation(Select.class) != null) {
			entryType = getEntryContainerType(method);

			return select(method.getAnnotation(Select.class), sqlFactoryHandler, args, returnType, entryType, method);
		}

		if (args != null && args[0] != null)
			entryType = args[0].getClass();// 实体类型由参数决定，因为 写入方法通常后面跟着的就是实体
		else
			throw new DaoException("DAO 接口方法:" + method + " 签名缺少实体参数！");

		if (method.getAnnotation(Insert.class) != null)
			return insert(method.getAnnotation(Insert.class), sqlFactoryHandler, args, returnType);

		if (method.getAnnotation(Update.class) != null)
			return update(method.getAnnotation(Update.class), sqlFactoryHandler, args);

		if (method.getAnnotation(Delete.class) != null)
			return delete(method.getAnnotation(Delete.class), sqlFactoryHandler, args);

		throw new DaoException("没有任何 DAO CRUD 的注解。你继承 IDAO 接口的子接口中，可能没有覆盖 IDAO 的方法" + method);
	}

	/**
	 * DAO 方法返回类型
	 * 
	 * @param method
	 * @return
	 * @throws DaoException
	 */
	private Class<?> getReturnType(Method method) throws DaoException {
		Class<?> clz = method.getReturnType();

		if (clz == Map.class || clz == List.class || clz == PageResult.class || clz == int.class || clz == Integer.class || clz == long.class || clz == Long.class || clz == String.class || clz == Boolean.class
				|| clz == boolean.class || clz.isArray())
			return clz;

		TableName t = getClz().getAnnotation(TableName.class);

		if (t == null && getBeanClz() == null) {
			throw new DaoException("请设置注解 TableName 的 beanClass 或送入 beanClz");
		} else {
			clz = getBeanClz() == null ? t.beanClass() : getBeanClz();
		}

		return clz;
	}

	/**
	 * 
	 * @param method
	 * @return
	 * @throws DaoException
	 */
	private Method getSqlFactoryHandler(Method method) throws DaoException {
		SqlFactory sqlFactory = method.getAnnotation(SqlFactory.class);

		if (sqlFactory != null) {
			String methodName = sqlFactory.value();

			Class<?> clz = sqlFactory.clz();
			if (clz == null || clz.equals(Object.class)) {
				clz = this.clz;
			}

			Method m = ReflectUtil.getMethod(clz, methodName, String.class);
			if (m == null)
				throw new DaoException("找不到对应的 SqlFactory，应该为 Function<String, String>");

			return m;
		}

		return null;
	}

	/**
	 * DAO 实际类引用，必须为接口
	 */
	private Class<?> clz;

	/**
	 * 实体类型
	 */
	private Class<?> beanClz;

	private String tableName;

	/**
	 * 绑定 DAO 的类，实例化该接口，返回实例
	 * 
	 * @param clz DAO 实际类引用，必须为接口
	 * @return DAO 实例
	 */
	@SuppressWarnings("unchecked")
	public <T extends IBaseDao<?>> T bind(Class<T> clz) {
		this.setClz(clz);
		// 获取注解的表名
		TableName tableNameA = clz.getAnnotation(TableName.class);

		if (tableNameA != null && !CommonUtil.isEmptyString(tableNameA.value())) {
			setTableName(tableNameA.value());
		}

		Object obj = Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, this);
		return (T) obj;
	}

	public <T extends IBaseDao<?>> T bind(Class<T> clz, String tableName) {
		setTableName(tableName);
		return bind(clz);
	}

	public <T extends IBaseDao<?>> T bind(Class<T> clz, String tableName, Class<?> beanClz) {
		setTableName(tableName);
		setBeanClz(beanClz);
		return bind(clz);
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
//					if ("com.ajaxjs.framework.Map<String, Object>".equals(typeArgument.getTypeName())) {
//						return Map<String, Object>.class;
//					}

					if ("T".equals(typeArgument.toString())) {
						TableName t = getClz().getAnnotation(TableName.class);
						if (t == null && getBeanClz() == null) {
							throw new DaoException("请设置注解 TableName 的 beanClass 或送入 beanClz");
						} else {
							return getBeanClz() == null ? t.beanClass() : getBeanClz();
						}

					} else if (typeArgument instanceof ParameterizedTypeImpl) {
						return Map.class; // 写死的
					} else
						return (Class<?>) typeArgument;
				}
			}
		}

		return type;
	}

	public String handleSql(String sql, Method sqlFactoryHandler) {
		if (sqlFactoryHandler != null)
			sql = ReflectUtil.executeStaticMethod(sqlFactoryHandler, sql).toString();

		if (tableName != null) {
			sql = sql.replaceAll("\\$\\{\\w+\\}", tableName);
		}

		return sql;
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
	private <R, B> Object select(Select select, Method sqlFactoryHandler, Object[] args, Class<R> returnType, Class<B> entryType, Method method) throws DaoException {
		String sql = isSqlite(select.sqliteValue(), conn) ? select.sqliteValue() : select.value();
		sql = handleSql(sql, sqlFactoryHandler);

		Object[] r = doSql(sql, method,args);
		sql = (String) r[0];
		args = (Object[]) r[1];

		Object result = null;

		if (returnType == int.class || returnType == Integer.class) {
			result = queryOne(conn, sql, int.class, args);
		} else if (returnType == long.class || returnType == Long.class) {
			result = queryOne(conn, sql, long.class, args);
		} else if (returnType == Integer[].class) {
			result = queryArray(conn, sql, Integer.class, args); // 不支持int[]
		} else if (returnType == String.class) {
			result = queryOne(conn, sql, String.class, args);
		} else if (returnType == List.class) {
			result = queryList(entryType, sql, args);
		} else if (returnType == PageResult.class) {// 分页
			result = PageResult.doPage(conn, entryType, select, sql, method, this, args, r[2]);
		} else if (returnType == Map.class) {
			result = queryAsMap(conn, sql, args);
		} else {
			// bean
			result = queryAsBean(returnType, conn, sql, args);
		}

		return result;
	}

	/**
	 * 传入一个匿名函数，转化 sql
	 * 
	 * @param sql
	 * @param method 
	 * @param args 参数有变动
	 * @return
	 */
	private static Object[] doSql(String sql, Method method, Object[] args) {
		Object[] _args = new Object[3];
		_args[0] = sql;
		_args[1] = args;
		
		
		if(args != null && args.length > 0) {
			String realSql = null;
			List<Object> list = new ArrayList<>();
			boolean found = false;
			
			Parameter[] parameters = method.getParameters();
			int i = 0;
			
			for (Object obj : args) {
				Parameter p = parameters[i++];
				if(Function.class.equals(p.getType())) {
					found = true;
					if (obj instanceof Function) {
						@SuppressWarnings("unchecked")
						Function<String, String> fn = (Function<String, String>) obj;
						realSql = fn.apply(sql);
						_args[2] = fn;
					} else {
						// obj is null;
						realSql = sql; // SQL 不变，去掉 fn 参数
						_args[2] = null;
					}
				} else {
					list.add(obj);
				}
			}
			
			if(found) {
				_args[0] = realSql;
				_args[1] = list.toArray();				
			}
		}
		
		return _args;
	}

	private Object queryList(Class<?> entryType, String sql, Object[] args) {
		Object result;

		if (entryType == Map.class) {
			result = queryAsMapList(conn, sql, args);
		} else {
			result = queryAsBeanList(entryType, conn, sql, args);
		}

		return result;
	}

	/**
	 * 判断是否 SQLite 数据库
	 * 
	 * @param sqliteValue SQLite 数据库专用的 SQL 语句
	 * @param conn 数据库连接对象
	 * @return true = 是 SQLite 数据库
	 */
	public static boolean isSqlite(String sqliteValue, Connection conn) {
		return !CommonUtil.isEmptyString(sqliteValue) && conn.toString().contains("sqlite");
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
	private <R> Serializable insert(Insert insert, Method sqlFactoryHandler, Object[] args, Class<R> returnType) {
		String sql = insert.value();
		Serializable id = null; // INSERT 返回新建的 id

		id = write(sql, sqlFactoryHandler, insert::tableName, _sql -> create(conn, _sql, args),
				tableName -> args[0] instanceof Map ? createMap(conn, (Map<String, Object>) args[0], tableName) : createBean(conn, args[0], tableName), args);

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
	private Integer update(Update update, Method sqlFactoryHandler, Object[] args) {
		String sql = update.value();

		@SuppressWarnings("unchecked")
		int effectRows = write(sql, sqlFactoryHandler, update::tableName, _sql -> update(conn, _sql, args),
				tableName -> args[0] instanceof Map ? updateMap(conn, (Map<String, Object>) args[0], tableName) : updateBean(conn, args[0], tableName), args);
		return effectRows;
	}

	private <T> T write(String sql, Method sqlFactoryHandler, Supplier<String> getTableNameByAnn, Function<String, T> writeSql, Function<String, T> writeBean, Object[] args) {
		T r = null;

		if (!sql.equals("")) { /* 以 sql 方式更新 */
			sql = handleSql(sql, sqlFactoryHandler);
			r = writeSql.apply(sql);
		} else if (sql.equals("") && args[0] != null) { // 以 bean 方式删除
			String tableName = getTableNameByAnn.get();// 表名可以通过注解获取（类），也可以直接 insert.tableName() 获取
			if (tableName == null || "".equals(tableName))
				tableName = getTableName();

			r = writeBean.apply(tableName);
		}

		return r;
	}

	/**
	 * 删除动作
	 * 
	 * @param delete 包含 SQL 的注解
	 * @param args SQL 参数
	 * @return 是否删除成功
	 */
	private Boolean delete(Delete delete, Method sqlFactoryHandler, Object[] args) {
		String sql = isSqlite(delete.sqliteValue(), conn) ? delete.sqliteValue() : delete.value();

		boolean isDeleted = write(sql, sqlFactoryHandler, delete::tableName, _sql -> update(conn, _sql, args) >= 1, tableName -> delete(conn, args[0], tableName), args);

		return isDeleted;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Class<?> getBeanClz() {
		return beanClz;
	}

	public void setBeanClz(Class<?> beanClz) {
		this.beanClz = beanClz;
	}
}
