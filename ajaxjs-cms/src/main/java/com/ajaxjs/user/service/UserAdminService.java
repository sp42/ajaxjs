package com.ajaxjs.user.service;

import java.util.List;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.user.model.User;

public interface UserAdminService extends IService<User, Long> {
	/**
	 * 获取最新发布的五条新闻
	 * 
	 * @return
	 */
	List<User> getTop5();

}
