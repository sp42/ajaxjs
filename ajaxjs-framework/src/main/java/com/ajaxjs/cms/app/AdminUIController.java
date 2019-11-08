package com.ajaxjs.cms.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;

/**
 * 后台界面
 * 
 * @author Administrator
 *
 */
@Path("/admin")
public class AdminUIController implements IController, Constant {
	@GET
	public String admin() {
		return jsp_perfix_webinf + "/admin/admin";
	}
	
	/**
	 * 后台首页
	 * @return
	 */
	@GET
	@Path("home")
	public String home() {
		return jsp_perfix_webinf + "/admin/home";
	}
}
