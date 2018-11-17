package com.ajaxjs.user.dao;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.model.UserCommonAuth;

public interface UserCommonAuthDao extends IDao<UserCommonAuth, Long> {
	final static String tableName = "user_common_auth";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public UserCommonAuth findById(Long id);

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<UserCommonAuth> findPagedList(QueryParams param, int start, int limit);

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