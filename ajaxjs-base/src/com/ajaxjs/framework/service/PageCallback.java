package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.util.List;

import com.ajaxjs.framework.dao.BaseDao;

public interface PageCallback<T, DAO extends BaseDao<T, ? extends Serializable>>{
	int getTotal(DAO dao);
	List<T> getList(int start, int limit, DAO dao);
}