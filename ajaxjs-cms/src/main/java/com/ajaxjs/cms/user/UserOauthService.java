package com.ajaxjs.cms.user;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;

@Bean
public class UserOauthService extends BaseService<UserOauth> {
	UserOauthDao dao = new Repository().bind(UserOauthDao.class);

	{
		setUiName("用户三方登录");
		setShortName("userOauth");
		setDao(dao);
	}
}