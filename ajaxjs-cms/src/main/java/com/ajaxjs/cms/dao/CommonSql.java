package com.ajaxjs.cms.dao;

public interface CommonSql {
	String findList_Online 	= "SELECT * FROM ${tableName} WHERE status = 1";
	String findList_All 	= "SELECT * FROM ${tableName}";
}
