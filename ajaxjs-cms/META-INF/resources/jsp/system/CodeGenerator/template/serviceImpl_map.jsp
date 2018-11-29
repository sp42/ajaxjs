<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.service;

import java.util.Map;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.ioc.Bean;

import ${packageName}.dao.${beanName}Dao;

@Bean(value = "${beanName}Service")
public class ${beanName}ServiceImpl implements ${beanName}Service {
	${beanName}Dao dao = new DaoHandler&lt;${beanName}Dao&gt;().bind(${beanName}Dao.class);

	@Override
	public Map&lt;String, Object&gt; findById(Integer id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Integer create(Map&lt;String, Object&gt; bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(Map&lt;String, Object&gt; bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Map&lt;String, Object&gt; bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult&lt;Map&lt;String, Object&gt;&gt; findPagedList(QueryParams params) throws ServiceException {
		return dao.findPagedList(params);
	}

	@Override
	public PageResult&lt;Map&lt;String, Object&gt;&gt; findPagedList(int start, int limit) throws ServiceException {
		return dao.findPagedList(new QueryParams(start, limit));
	}

	@Override
	public String getName() {
		return "${tablesCommentChinese}";
	}

	@Override
	public String getTableName() {
		return "${tableName}";
	} 

}