package com.ajaxjs.cms.user;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "user", beanClass = User.class)
public interface UserDao extends IBaseDao<User> {
	@Select("SELECT id, avatar, name, username, sex, birthday, email, phone, createDate, location, " + selectCover + " AS cover FROM ${tableName} entry")
	@Override
	public PageResult<User> findPagedList_Cover(int start, int limit);
}