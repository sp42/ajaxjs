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
package com.ajaxjs.sql.orm;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.util.StringUtils;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.DataBaseType;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.SqlFactory;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.annotation.Update;
import com.ajaxjs.sql.orm.model.ArgsInfo;
import com.ajaxjs.sql.orm.model.DaoInfo;

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

	/**
	 * 检查是否已经存在数据库连接对象，并且如是执行 toString() 方法，那么返回 true
	 * 
	 * @param method DAO 方法对象
	 * @return true 表示为执行 toString() 方法
	 */
	boolean init(Method method) {
		conn = JdbcConnection.getConnection(); // 从线程中获数据库连接对象

		if (conn == null)
			throw new NullPointerException("没有 connection， 请先建立数据库连接对象。"); // 再检查

		return method.getName().equals("toString"); // 没有默认的 toString() 这里实现一个
	}

	/**
	 * 绑定 DAO 的类，实例化该接口，返回实例
	 * 
	 * @param <T> 实体类型
	 * @param clz DAO 实际类引用，必须为接口
	 * @return DAO 实例
	 */
	@SuppressWarnings("unchecked")
	public <T extends IBaseDao<?>> T bind(Class<T> clz) {
		setClz(clz);

		// 获取注解的表名
		TableName tableNameA = clz.getAnnotation(TableName.class);

		if (tableNameA != null && StringUtils.hasText(tableNameA.value()))
			setTableName(tableNameA.value());

		Object obj = Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, this);

		return (T) obj;
	}

	/**
	 * 方法对象是否带有特定的注解
	 * 
	 * @param method
	 * @param a
	 * @return
	 */
	private static boolean isNull(Method method, Class<? extends Annotation> a) {
		return method.getAnnotation(a) != null;
	}

	/**
	 * 高阶函数
	 * 
	 * @param a
	 * @return
	 */
	private static Function<Method, Boolean> higherOrderFn(Class<? extends Annotation> a) {
		return method -> isNull(method, a);
	}

	static Function<Method, Boolean> isRead = higherOrderFn(Select.class), isCreate = higherOrderFn(Insert.class), isUpdate = higherOrderFn(Update.class),
			isDelete = higherOrderFn(Delete.class);

	static boolean isRead(Method method) {
		return isNull(method, Select.class);
	}

	static boolean isCreate(Method method) {
		return isNull(method, Insert.class);
	}

	static boolean isUpdate(Method method) {
		return isNull(method, Update.class);
	}

	static boolean isDelete(Method method) {
		return isNull(method, Delete.class);
	}

	/**
	 * DAO 方法返回类型
	 * 
	 * @param method DAO 方法对象
	 * @return DAO 方法返回类型 @
	 */
	Class<?> getReturnType(Method method) {
		Class<?> clz = method.getReturnType();

		if (clz == Map.class || clz == List.class || clz == PageResult.class || clz == int.class || clz == Integer.class || clz == long.class || clz == Long.class
				|| clz == String.class || clz == Boolean.class || clz == boolean.class || clz.isArray())
			return clz;

		TableName t = getClz().getAnnotation(TableName.class);

		if (t == null && getBeanClz() == null)
			throw new Error("请设置注解 TableName 的 beanClass 或送入 beanClz");
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
	 * @return 处理过的 SQL 语句 @ DAO 异常
	 */
	ArgsInfo handleSql(String sql, Method method, Object[] args) {
		if (method != null && method.getAnnotation(SqlFactory.class) != null)
			sql = DaoSqlHandler.doSqlFactory(sql, method, getClz());

		if (getTableName() != null) // 替换 ${tableName}为真实的表名
			sql = sql.replaceAll("\\$\\{\\w+\\}", getTableName());

		return DaoSqlHandler.doSql(sql, method, args);
	}

	/**
	 * @param getSql
	 * @param getTableName
	 * @param args
	 * @param writeSql
	 * @param writeBean
	 * @return @
	 */
	private <T> T getFn(Supplier<String> getSql, Supplier<String> getTableName, Object[] args, Method method, Function<DaoInfo, T> writeSql, Function<DaoInfo, T> writeBean) {
		Object bean = args[0];

		DaoInfo daoInfo = new DaoInfo();
		daoInfo.sql = getSql == null ? "" : getSql.get();
		// 表名可以通过注解获取（类），也可以直接 insert.tableName() 获取
		daoInfo.tableName = (getTableName == null || !StringUtils.hasText(getTableName.get())) ? getTableName() : getTableName.get();
		daoInfo.isMap = bean instanceof Map;
		daoInfo.bean = bean;

		if (StringUtils.hasText(daoInfo.sql)) { /* 以 sql 方式更新 */
			daoInfo.sql = handleSql(daoInfo.sql, method, args).sql;

			return writeSql.apply(daoInfo);
		} else if (!StringUtils.hasText(daoInfo.sql) && bean != null) // 以 bean 方式删除
			return writeBean.apply(daoInfo);

		throw new Error("程序错误");
	}

	@SuppressWarnings("unchecked")
	private Function<DaoInfo, Serializable> createEntity = daoInfo -> daoInfo.isMap ? createMap(conn, (Map<String, Object>) daoInfo.bean, daoInfo.tableName)
			: createBean(conn, daoInfo.bean, daoInfo.tableName);

	@SuppressWarnings("unchecked")
	private Function<DaoInfo, Integer> updateEntity = daoInfo -> daoInfo.isMap ? updateMap(conn, (Map<String, Object>) daoInfo.bean, daoInfo.tableName)
			: updateBean(conn, daoInfo.bean, daoInfo.tableName);

	/**
	 * 新增动作
	 *
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return 自增 id @
	 */
	protected <R> Serializable insert(Method method, Object[] args) {
		Class<?> returnType = getReturnType(method);
		Insert insert = method.getAnnotation(Insert.class);
		Function<DaoInfo, Serializable> writeSql = daoInfo -> create(conn, daoInfo.sql, args);

		// INSERT 返回新建的 id
		Serializable id = insert == null ? getFn(null, null, args, method, writeSql, createEntity) : getFn(insert::value, insert::tableName, args, method, writeSql, createEntity);

		if (id.getClass() == BigInteger.class)// mysql driver 8 returns BigInteger
			id = ((BigInteger) id).longValue();

		if ((returnType == Integer.class || returnType == int.class) && id.getClass() == Long.class)
			return Integer.parseInt("" + id);
		else if ((returnType == Long.class || returnType == long.class) && id.getClass() == Integer.class)
			return new Long((Integer) id);
		else
			return id;
	}

	/**
	 * 更新动作
	 *
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return 影响的行数 @
	 */
	protected int update(Method method, Object[] args) {
		Update update = method.getAnnotation(Update.class);
		Function<DaoInfo, Integer> writeSql = daoInfo -> update(conn, daoInfo.sql, args);

		return update == null ? getFn(null, null, args, method, writeSql, updateEntity) : getFn(update::value, update::tableName, args, method, writeSql, updateEntity);
	}

	/**
	 * 删除动作
	 *
	 * @param method DAO 方法对象
	 * @param args   DAO 方法的参数
	 * @return 是否删除成功 @
	 */
	protected Boolean delete(Method method, Object[] args) {
		Delete delete = method.getAnnotation(Delete.class);
		Supplier<String> getSql = JdbcConnection.getDaoContext().getDbType() == DataBaseType.SQLITE ? delete::sqliteValue : delete::value;

		/* DELETESQL 也是用 update方法 */
		return getFn(getSql, delete::tableName, args, method, daoInfo -> update(conn, daoInfo.sql, args) >= 1, daoInfo -> delete(conn, daoInfo.bean, daoInfo.tableName));
	}

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
}
