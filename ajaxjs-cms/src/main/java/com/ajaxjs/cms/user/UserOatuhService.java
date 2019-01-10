package com.ajaxjs.cms.user;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("UserService")
public class UserOatuhService extends BaseService<User> implements UserService {
	UserDao dao = new Repository().bind(UserDao.class);

	{
		setUiName("用户");
		setShortName("user");
		setDao(dao);
	}
}