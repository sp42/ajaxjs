package com.ajaxjs.user.dao;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.model.UserOauth;

public interface UserOauthDao extends IDao<UserOauth, Long> {
	final static String tableName = "user_oauth";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public UserOauth findById(Long id);
	
	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<UserOauth> findPagedList(QueryParams param, int start, int limit);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(UserOauth entry);

	@Update(tableName = tableName)
	@Override
	public int update(UserOauth entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(UserOauth entry);
}