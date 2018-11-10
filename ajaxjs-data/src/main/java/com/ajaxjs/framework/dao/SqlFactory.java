package com.ajaxjs.framework.dao;

/**
 * SQL 工厂，为满足不同业务的 SQL，需要对 SQL 字符串作自定义的加工处理，在这接口的 toSql 方法中实现
 * 
 * @author Frank Cheung
 *
 */
public interface SqlFactory {
	/**
	 * SQL 工厂
	 * 
	 * @param s 返回 JDBC Prepared Statement 所用的参数，对应一个或多个 ? （问号）。 通常这个返回值在 toSql()
	 * 方法中处理，由于数组是按地址引用，所以此方法里面修改了数组会影响
	 * @return SqlAndArgs
	 */
	public SqlAndArgs toSql(SqlAndArgs s);
}
