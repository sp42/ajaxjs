package com.ajaxjs.framework.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ajaxjs.util.LogHelper;

@Controller
@RequestMapping(value = "/cms/user")
public class UserInfoController {
	private static final LogHelper LOGGER = LogHelper.getLog(UserInfoController.class);
	
//	private Service service = new Service();
	
	/**
	 * 登录界面
	 * @return
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profileUI() {
		LOGGER.info("profile界面");
		return "common/user/profile";
	}
	
	/**
	 * 修改用户信息
	 * @return
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String updateProfile() {
		LOGGER.info("updateProfile");
		return "common/user/login";
	}
	
	/**
	 * 登录信息界面
	 * @return
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
	public String updatePasswordUI() {
		LOGGER.info("登录信息界面");
		return "common/user/updatePassword";
	}
	
	/**
	 * 修改登录信息
	 * @return
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public String updatePassword() {
		LOGGER.info("修改登录信息");
		return "common/user/login";
	}

}
