package com.ajaxjs.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;

/**
 * 后台界面
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin")
public class AdminUIController implements IController, Constant {
	@GET
	@Path("login")
	public String login() {
		return BaseController.jsp("admin/admin-login");
	}

	@GET
	public String admin() {
		return BaseController.jsp("admin/admin");
	}

	/**
	 * 后台首页
	 * 
	 * @return
	 */
	@GET
	@Path("home")
	public String home() {
		return BaseController.jsp("admin/home");
	}

}
