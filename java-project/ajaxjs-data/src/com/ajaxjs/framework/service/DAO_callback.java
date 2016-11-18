package com.ajaxjs.framework.service;

import java.util.List;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.Query;

public interface DAO_callback<T extends BaseModel, Mapper extends DAO<T>> {

	public List<T> doIt(Mapper dao, int start, int limit, String sql_TableName, Query query);
}
