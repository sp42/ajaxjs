package com.ajaxjs.cms.user;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean("UserLoginLogService")
public class UserLoginLogService extends BaseService<UserLoginLog>  {
	public UserLoginLogDao dao = new Repository().bind(UserLoginLogDao.class);
	
	{
		setUiName("用户登录日志");
		setShortName("userLoginLog");
		setDao(dao);
	}

	
	
}