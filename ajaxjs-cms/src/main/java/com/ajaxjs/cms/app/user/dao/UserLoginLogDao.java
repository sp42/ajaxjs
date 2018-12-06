package com.ajaxjs.cms.app.user.dao;

import com.ajaxjs.cms.app.user.model.UserLoginLog;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface UserLoginLogDao extends IDao<UserLoginLog, Long> {
	final static String tableName = "user_login_log";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public UserLoginLog findById(Long id);
	
	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<UserLoginLog> findPagedList(int start, int limit);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(UserLoginLog entry);

	@Update(tableName = tableName)
	@Override
	public int update(UserLoginLog entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(UserLoginLog entry);
}