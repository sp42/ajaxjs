package com.ajaxjs.cms.user;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.dao.PageResult;

@TableName(value = "user_login_log", beanClass = UserLoginLog.class)
public interface UserLoginLogDao extends IBaseDao<UserLoginLog> {
	@Select("SELECT e.*, user.username AS userName FROM ${tableName} e LEFT JOIN user ON user.id = e.userId WHERE e.userId = ?")
	public PageResult<UserLoginLog> findUserLoginLogByUserId(long userId, int start, int limit);
}