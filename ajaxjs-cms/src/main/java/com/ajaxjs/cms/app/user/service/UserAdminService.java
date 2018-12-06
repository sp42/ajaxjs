package com.ajaxjs.cms.app.user.service;

import java.util.List;

import com.ajaxjs.cms.app.user.model.User;
import com.ajaxjs.framework.service.IService;

public interface UserAdminService extends IService<User, Long> {
	/**
	 * 获取最新发布的五条新闻
	 * 
	 * @return
	 */
	List<User> getTop5();

}
