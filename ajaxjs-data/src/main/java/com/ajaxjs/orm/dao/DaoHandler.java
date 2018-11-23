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
package com.ajaxjs.orm.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.SqlFactory;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Data Access Object 负责对数据库的增删改查工作最后的工作。 框架中一般无须写出实现，提供接口即可。 通过 Java
 * 代理自动实现接口的实例 必须是接口原类型，而不是接口其父类，也就是不支持多态
 * 
 * @author Sp42 frank@ajaxjs.com
 * @param <T> DAO 实际类型引用
 */
@SuppressWarnings("restriction")
public class DaoHandler<T> extends JdbcHelper implements InvocationHandler {

	/**
	 * 数据库连接对象。You should put connection by calling
	 * JdbcConnection.setConnection(conn).
	 */
	private Connection conn;

	/**
	 * 执行时的调用。不管执行哪个方法都会调用该方法。
	 * 
	 * @throws DaoException DAO 异常
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws DaoException {
		if (method.getName().equals("toString")) // 没有默认的 toString() 这里实现一个
			return "This is a AJAXJS DAO.";

		conn = JdbcConnection.getConnection(); // 从线程中获数据库连接对象

		if (conn == null)
			throw new DaoException("没有 connection， 请先建立数据库连接对象。"); // 再检查

		Class<?> returnType = method.getReturnType(), entryType = null; // DAO 分法返回的类型；实体类型

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

	private Class<T> clz;

	/**
	 * 绑定 DAO 的类，实例化该接口，返回实例
	 * 
	 * @param clz DAO 实际类引用，必须为接口
	 * @return DAO 实例
	 */
	@SuppressWarnings("unchecked")
	public T bind(Class<T> clz) {
		this.setClz(clz);
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
	private <R, B> Object select(Select select, Method sqlFactoryHandler, Object[] args, Class<R> returnType, Class<B> entryType, Method method) throws DaoException {
		String sql = isSqlite(select.sqliteValue(), conn) ? select.sqliteValue() : select.value();

		if (sqlFactoryHandler != null)
			sql = ReflectUtil.executeStaticMethod(sqlFactoryHandler, sql).toString();

		Object result = null;

		if (returnType == int.class) {
			result = queryOne(conn, sql, int.class, args);
		} else if (returnType == Integer[].class) {
			result = queryArray(conn, sql, Integer.class, args); // 不支持int[]
		} else if (returnType == String.class) {
			result = queryOne(conn, sql, String.class, args);
		} else if (returnType == List.class) {
			result = entryType == Map.class ? queryAsMapList(conn, sql, args) : queryAsBeanList(entryType, conn, sql, args);
		} else if (returnType == PageResult.class) {// 分页
//				queryParam.order = new HashMap<>(); // 默认按照 id 排序
//				queryParam.order.put("id", "DESC");
//				sql = queryParam.orderToSql(sql);
			
			result = PageResult.doPage(conn, entryType, select, sql, method, args);
		} else if (returnType == Map.class) {
			result = queryAsMap(conn, sql, args);
		} else {
			// bean
			result = queryAsBean(returnType, conn, sql, args);
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
		Serializable id = null; // INSERT 返回新建的 id

		if (!insert.value().equals("")) { /* 以 sql 方式创建 */
			String sql = insert.value();
			if (sqlFactoryHandler != null)
				sql = ReflectUtil.executeStaticMethod(sqlFactoryHandler, sql).toString();

			id = create(conn, sql, args);
		} else if (insert.value().equals("") && insert.tableName() != null && args[0] != null) {// 以 bean 方式创建
			if (args[0] instanceof Map)
				id = createMap(conn, (Map<String, Object>) args[0], insert.tableName());
			else
				id = createBean(conn, args[0], insert.tableName());
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
	private Integer update(Update update, Method sqlFactoryHandler, Object[] args) {
		int effectRows = 0;

		if (!update.value().equals("")) { /* 以 sql 方式更新 */
			String sql = update.value();
			if (sqlFactoryHandler != null)
				sql = ReflectUtil.executeStaticMethod(sqlFactoryHandler, sql).toString();

			effectRows = update(conn, sql, args);
		} else if (test(update::value, update::tableName, args[0])) { // 以 bean 方式删除
			if (args[0] instanceof Map)
				effectRows = updateMap(conn, (Map<String, Object>) args[0], update.tableName());
			else
				effectRows = updateBean(conn, args[0], update.tableName());
		}

		return effectRows;
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

		if (sqlFactoryHandler != null)
			sql = ReflectUtil.executeStaticMethod(sqlFactoryHandler, sql).toString();

		if (!CommonUtil.isEmptyString(sql)) { /* 以 sql 方式删除 */
			return update(conn, sql, args) >= 1;
		} else if (test(delete::value, delete::tableName, args[0])) { // 以 bean 方式删除
			return delete(conn, args[0], delete.tableName());
		} else
			return false;
	}

	private static boolean test(Supplier<String> crud, Supplier<String> getTableName, Object entryContainer) {
		return crud.get().equals("") && getTableName.get() != null && entryContainer != null;
	}

	public Class<T> getClz() {
		return clz;
	}

	public void setClz(Class<T> clz) {
		this.clz = clz;
	}
}
