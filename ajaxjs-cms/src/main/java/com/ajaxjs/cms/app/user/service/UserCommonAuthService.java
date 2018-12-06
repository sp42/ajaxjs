package com.ajaxjs.cms.app.user.service;

import com.ajaxjs.cms.app.user.model.UserCommonAuth;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public interface UserCommonAuthService extends IService<UserCommonAuth, Long> {

	/**
	 * 更新用户密码
	 * 
	 * @param userId
	 * @param password
	 * @param new_password
	 * @return
	 */
	boolean updatePwd(UserCommonAuth auth, String new_password) throws ServiceException;

}