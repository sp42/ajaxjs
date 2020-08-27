package com.ajaxjs.user;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserDao; 
import com.ajaxjs.util.logger.LogHelper;

public abstract class BaseUserService extends BaseService<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserService.class);

	public static UserDao DAO = new Repository().bind(UserDao.class);

	{
		setUiName("用户");
		setShortName("user");
		setDao(DAO);
	}
}
