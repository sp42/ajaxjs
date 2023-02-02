package com.ajaxjs.my_data_framework.enmu_field;

/**
 * 数据库类型
 * 
 * @author Frank Cheung
 *
 */
public enum DB_Type implements IntegerValueEnum {
	MYSQL, SQLITE, SQLSERVER, ORACLE, SPARK;

	@Override
	public Integer getValue() {
		return ordinal();
	}

}
