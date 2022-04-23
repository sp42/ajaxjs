package com.ajaxjs.user.common.service;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.user.UserAuth;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.common.model.LogLogin;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface UserDAO extends UserCommonDAO {
	default UserAuth findPswByUserId(long id) {
		return UserAuthDAO.setWhereQuery("loginType = 1 AND userId = " + id).findOne();
	}

	static interface LogLoginDAO extends IDataService<LogLogin> {
	}

	public static final LogLoginDAO LogLoginDAO = new Caller("cms", "user_login_log").bind(LogLoginDAO.class, LogLogin.class);
}
