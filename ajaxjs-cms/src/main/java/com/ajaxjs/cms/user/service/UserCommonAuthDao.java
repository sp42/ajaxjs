package com.ajaxjs.cms.user.service;

import com.ajaxjs.cms.user.UserCommonAuth;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;

public interface UserCommonAuthDao extends IBaseDao<UserCommonAuth> {
	final static String tableName = "user_common_auth";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public UserCommonAuth findById(Long id);

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<UserCommonAuth> findPagedList(int start, int limit);

	@Insert(tableName = tableName)
	@Override
	public Long create(UserCommonAuth entry);

	@Update(tableName = tableName)
	@Override
	public int update(UserCommonAuth entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(UserCommonAuth entry);

	@Select("SELECT * FROM " + tableName + " WHERE userId = ?")
	public UserCommonAuth findByUserId(Long id);

	@Delete("DELETE FROM " + tableName + " WHERE userId = ?")
	public boolean deleteByUserId(Long userId);
}