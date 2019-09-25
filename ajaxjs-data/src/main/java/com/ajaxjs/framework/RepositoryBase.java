package com.ajaxjs.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.function.BiFunction;

import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.util.CommonUtil;

public abstract class RepositoryBase extends JdbcHelper {
	/**
	 * 数据库连接对象。You should put connection by calling JdbcConnection.setConnection(conn).
	 */
	Connection conn;

	/**
	 * DAO 实际类引用，必须为接口
	 */
	private Class<?> clz;

	/**
	 * 实体类型
	 */
	private Class<?> beanClz;

	/**
	 * SQL 表名称
	 */
	private String tableName;

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

	/**
	 * 判断是否 SQLite 数据库
	 * 
	 * @param sqliteValue 	SQLite 数据库专用的 SQL 语句
	 * @param conn 			数据库连接对象
	 * @return true = 是 SQLite 数据库
	 */
	static boolean isSqlite(String sqliteValue, Connection conn) {
		return !CommonUtil.isEmptyString(sqliteValue) && conn.toString().contains("sqlite");
	}

	private static BiFunction<Method, Class<? extends Annotation>, Boolean> isNull = (method,
			a) -> method.getAnnotation(a) != null;

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
}
