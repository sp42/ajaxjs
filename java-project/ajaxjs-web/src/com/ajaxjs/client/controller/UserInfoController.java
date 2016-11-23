package com.ajaxjs.client.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.common.Read_Create_Controller;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserService;
import com.ajaxjs.util.LogHelper;

@Controller
@Path("/user")
public class UserInfoController extends Read_Create_Controller<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserInfoController.class);
	
	public UserInfoController(){
		setJSON_output(true);
		setService(new UserService());
	}
	
	public String logout(){
		LOGGER.info("用户登出");
		
		return "";
	}
	
	@POST
	@Override
	public String create(User entity, ModelAndView model) {
		LOGGER.info("创建用户");
		return super.create(entity, model);
	}
}
