package com.ajaxjs.framework.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MyBatisDao<T, ID extends Serializable> extends BaseDao<T, ID> {
	String tableName = "news";
	
	@Select("SELECT Count(*) AS total FROM news")
	@Override
	int count();
	 
	
 
}
