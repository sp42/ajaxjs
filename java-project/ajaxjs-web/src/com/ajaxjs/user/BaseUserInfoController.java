package com.ajaxjs.user;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.LogHelper;

public abstract class BaseUserInfoController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserInfoController.class);
	
	public static final String perfix = "/WEB-INF/jsp/";
	
//	private Service service = new Service();
	
	/**
	 * 登录界面
	 * @return
	 */
	@GET
	@Path("/profile")
	public String profileUI() {
		LOGGER.info("profile界面");
		return perfix + perfix + "common/user/profile";
	}
	
	/**
	 * 修改用户信息
	 * @return
	 */
	@POST
	@Path("/profile")
	public String updateProfile() {
		LOGGER.info("updateProfile");
		return perfix + "common/user/login";
	}
	
	/**
	 * 登录信息界面
	 * @return
	 */
	@GET
	@Path("/updatePassword")
	public String updatePasswordUI() {
		LOGGER.info("登录信息界面");
		return perfix + "common/user/updatePassword";
	}
	
	/**
	 * 修改登录信息
	 * @return
	 */
	@POST
	@Path("/updatePassword")
	public String updatePassword() {
		LOGGER.info("修改登录信息");
		return perfix + "common/user/login";
	}

}
