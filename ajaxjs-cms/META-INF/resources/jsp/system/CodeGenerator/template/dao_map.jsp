&lt;%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;package ${packageName}.dao;

import java.util.Map;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.jdbc.PageResult;

public interface ${beanName}Dao extends IDao&lt;Map&lt;String, Object&gt;, Integer&gt; {
	final static String tableName = "${tableName}";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Map&lt;String, Object&gt; findById(Integer id);
	
	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult&lt;Map&lt;String, Object&gt;&gt; findPagedList(QueryParams param);
	
	@Insert(tableName = tableName)
	@Override
	public Integer create(Map&lt;String, Object&gt; map);

	@Update(tableName = tableName)
	@Override
	public int update(Map&lt;String, Object&gt; map);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Map&lt;String, Object&gt; map);
}