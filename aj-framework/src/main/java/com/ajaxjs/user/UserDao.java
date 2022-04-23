package com.ajaxjs.user;

import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "user", beanClass = User.class)
public interface UserDao extends IBaseDao<User> {
	@Select("SELECT u.* FROM ${tableName} u INNER JOIN user_auth o ON u.id = o.userId WHERE loginType = 3 AND o.identifier = ?")
	public User findUserByOauthId(String identifier);
}