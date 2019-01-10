package com.ajaxjs.cms.user;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "user_oauth", beanClass = UserOauth.class)
public interface UserOauthDao extends IBaseDao<UserOauth> {
}