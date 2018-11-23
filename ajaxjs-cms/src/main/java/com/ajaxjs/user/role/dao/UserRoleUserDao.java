package com.ajaxjs.user.role.dao;

import java.util.Map;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface UserRoleUserDao extends IDao<Map<String, Object>, Integer> {
	final static String tableName = "user_admin";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Map<String, Object> findById(Integer id);
	
	@Select(value = "SELECT user_admin.*,  " + UserRoleRoleDao.tableName +".name AS roleName FROM " + tableName + " INNER JOIN " + UserRoleRoleDao.tableName + " ON " + UserRoleRoleDao.tableName + ".id = " + tableName + ".roleId")
	@Override
	public PageResult<Map<String, Object>> findPagedList( int start, int limit);
	
	@Insert(tableName = tableName)
	@Override
	public Integer create(Map<String, Object> map);

	@Update(tableName = tableName)
	@Override
	public int update(Map<String, Object> map);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Map<String, Object> map);
}