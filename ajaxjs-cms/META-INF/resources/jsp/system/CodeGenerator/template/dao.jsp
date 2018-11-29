<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.dao;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.jdbc.PageResult;
import ${packageName}.model.${beanName};

public interface ${beanName}Dao extends IDao<${beanName}, Long> {
	final static String tableName = "${tableName}";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public ${beanName} findById(Long id);
	
	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<${beanName}> findPagedList(QueryParams param);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(${beanName} entry);

	@Update(tableName = tableName)
	@Override
	public int update(${beanName} entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(${beanName} entry);
}