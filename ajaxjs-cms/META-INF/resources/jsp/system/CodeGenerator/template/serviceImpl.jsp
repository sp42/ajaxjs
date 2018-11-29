<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.service;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;
import ${packageName}.dao.${beanName}Dao;
import ${packageName}.model.${beanName};
import com.ajaxjs.ioc.Bean;

@Bean(value = "${beanName}Service")
public class ${beanName}ServiceImpl implements ${beanName}Service {
	${beanName}Dao dao = new DaoHandler<${beanName}Dao>().bind(${beanName}Dao.class);

	@Override
	public ${beanName} findById(Long id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Long create(${beanName} bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(${beanName} bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(${beanName} bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult<${beanName}> findPagedList(QueryParams params) throws ServiceException {
		return dao.findPagedList(params);
	}

	@Override
	public PageResult<${beanName}> findPagedList(int start, int limit) throws ServiceException {
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