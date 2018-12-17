<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>package ${packageName}.dao;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;
import ${packageName}.model.${beanName};

@TableName("${tableName}")
public interface ${beanName}Dao extends IDao<${beanName}, Long> {
	@Select("SELECT * FROM $/{tableName} WHERE id = ?")
	@Override
	public ${beanName} findById(Long id);
	
	@Select("SELECT * FROM $/{tableName}")
	@Override
	public PageResult<${beanName}> findPagedList(int start, int limit);
	
	@Insert
	@Override
	public Long create(${beanName} entry);

	@Update
	@Override
	public int update(${beanName} entry);

	@Delete
	@Override
	public boolean delete(${beanName} entry);
}