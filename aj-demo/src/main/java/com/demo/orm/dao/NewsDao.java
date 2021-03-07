package com.demo.orm.dao;

import java.util.List;

import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "news", beanClass = News.class)
public interface NewsDao extends IBaseDao<News> {
	@Select("SELECT * FROM ${tableName}")
	public List<News> findList();
}