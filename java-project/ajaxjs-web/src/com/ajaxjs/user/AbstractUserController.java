package com.ajaxjs.user;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.common.Read_Create_Controller;
import com.ajaxjs.util.LogHelper;

/**
 * 用户信息的增加、修改
 * @author frank
 *
 */
public abstract class AbstractUserController extends Read_Create_Controller<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractUserController.class);

	@GET
	@Path("/{id}")
	@Override
	public String getById(@PathParam("id")long id, ModelAndView model) {
		super.getById(id, model);
		return "/common_jsp/json/showInfo.jsp";
	}
	
	@PUT
	@Override
	public String create(User user, ModelAndView model) {
		LOGGER.info("创建用户");
		
		return super.create(user, model);
	}
	
	@POST
	@Override
	public String update(@PathParam("id") long id, User user, ModelAndView model) {
		LOGGER.info("修改用户");
		return "common/user/profile";
	}
}
