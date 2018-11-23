package com.ajaxjs.framework;

import java.io.Serializable;

import com.ajaxjs.orm.dao.IDao;

public interface BaseDao<T, ID extends Serializable> extends IDao<T, ID> {
	static String findById = "SELECT * FROM ${tableName} WHERE id = ?";
}
