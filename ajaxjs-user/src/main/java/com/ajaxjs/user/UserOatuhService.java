package com.ajaxjs.user;

import com.ajaxjs.user.service.UserDao;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("UserOatuhService")
public class UserOatuhService extends BaseService<User> {
	UserDao dao = new Repository().bind(UserDao.class);

	{
		setUiName("用户");
		setShortName("user");
		setDao(dao);
	}
}