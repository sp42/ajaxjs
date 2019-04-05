package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;

@Path("/admin")
public class AdminUIController implements IController, Constant {
	@GET
	public String admin() {
		return BaseController.cms("admin");
	}

}
