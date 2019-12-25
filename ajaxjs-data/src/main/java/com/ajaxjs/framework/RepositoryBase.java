/**
 * Copyright sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.JdbcUtil;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.util.CommonUtil;

/**
 * DAO 基类，包含工具方法
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class RepositoryBase extends JdbcHelper implements InvocationHandler {
	/**
	 * 数据库连接对象。You should put connection by calling
	 * JdbcConnection.setConnection(conn).
	 */
	Connection conn;

	/**
	 * DAO 实际类引用，必须为接口
	 */
	private Class<? extends IBaseDao<?>> clz;

	/**
	 * 实体类型
	 */
	private Class<?> beanClz;

	/**
	 * SQL 表名称
	 */
	private String tableName;

	public Class<? extends IBaseDao<?>> getClz() {
		return clz;
	}

	public void setClz(Class<? extends IBaseDao<?>> clz) {
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

	/**
	 * 检查是否已经存在数据库连接对象，并且如是执行 toString() 方法，那么返回 true
	 * 
	 * @param method DAO 方法对象
	 * @return true 表示为执行 toString() 方法
	 * @throws DaoException
	 */
	boolean init(Method method) throws DaoException {
		conn = JdbcConnection.getConnection(); // 从线程中获数据库连接对象

		if (conn == null)
			throw new DaoException("没有 connection， 请先建立数据库连接对象。"); // 再检查

		return method.getName().equals("toString"); // 没有默认的 toString() 这里实现一个
	}

	/**
	 * 绑定 DAO 的类，实例化该接口，返回实例
	 * 
	 * @param clz DAO 实际类引用，必须为接口
	 * @return DAO 实例
	 */
	@SuppressWarnings("unchecked")
	public <T extends IBaseDao<?>> T bind(Class<T> clz) {
		setClz(clz);

		// 获取注解的表名
		TableName tableNameA = clz.getAnnotation(TableName.class);

		if (tableNameA != null && !CommonUtil.isEmptyString(tableNameA.value()))
			setTableName(tableNameA.value());

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

	private static BiFunction<Method, Class<? extends Annotation>, Boolean> isNull = (method,
			a) -> method.getAnnotation(a) != null;

	private static Function<Class<? extends Annotation>, Function<Method, Boolean>> higherOrderFn = a -> {
		return method -> isNull.apply(method, a);
	};

	static Function<Method, Boolean> isRead = higherOrderFn.apply(Select.class),
			isCreate = higherOrderFn.apply(Insert.class), isUpdate = higherOrderFn.apply(Update.class),
			isDelete = higherOrderFn.apply(Delete.class);

	static boolean isRead(Method method) {
		return isNull.apply(method, Select.class);
	}

	static boolean isCreate(Method method) {
		return isNull.apply(method, Insert.class);
	}

	static boolean isUpdate(Method method) {
		return isNull.apply(method, Update.class);
	}

	static boolean isDelete(Method method) {
		return isNull.apply(method, Delete.class);
	}

	/**
	 * 判断是否有专属 SQLite 数据库的 SQL
	 * 
	 * @param sqliteValue SQLite 数据库专用的 SQL 语句
	 * @param conn        数据库连接对象
	 * @return true = 是 SQLite 数据库
	 */
	static boolean isSqlite(String sqliteValue, Connection conn) {
		return !CommonUtil.isEmptyString(sqliteValue) && JdbcUtil.isSqlite(conn);
	}
}
