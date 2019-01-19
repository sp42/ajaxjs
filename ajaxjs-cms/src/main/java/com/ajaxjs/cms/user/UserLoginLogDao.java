package com.ajaxjs.cms.user;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "user_login_log", beanClass = UserLoginLog.class)
public interface UserLoginLogDao extends IBaseDao<UserLoginLog> {
	final static String sql = "SELECT e.*, user.username AS userName FROM ${tableName} e LEFT JOIN user ON user.id = e.userId";

	@Select(sql)
	@Override
	public PageResult<UserLoginLog> findPagedList(int start, int limit);

	@Select(sql + " WHERE e.userId = ?")
	public PageResult<UserLoginLog> findUserLoginLogByUserId(long userId, int start, int limit);
}