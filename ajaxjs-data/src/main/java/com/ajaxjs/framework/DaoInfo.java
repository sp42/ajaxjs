package com.ajaxjs.framework;

public class DaoInfo {
	String tableName; 	// 表名
	String sql;			// 执行的 SQL 语句
	Object bean;		// Bean
	boolean isMap;		// true = map, false = bean
}
