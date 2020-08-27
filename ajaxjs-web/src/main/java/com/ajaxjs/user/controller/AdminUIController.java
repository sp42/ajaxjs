package com.ajaxjs.user.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.web.mvc.controller.IController;

/**
 * 后台界面
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin")
public class AdminUIController implements IController {
	@GET
	public String admin() {
		return BaseController.jsp("admin/admin");
	}

	@GET
	@Path("login")
	public String login() {
		return BaseController.jsp("admin/admin-login");
	}

	@GET
	@Path("home")
	public String home() {
		return BaseController.jsp("admin/home");
	}
}
