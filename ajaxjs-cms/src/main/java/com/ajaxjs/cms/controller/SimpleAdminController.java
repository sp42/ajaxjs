package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;

/**
 * for demo, simple use
 *
 */
@Path("/admin")
public class SimpleAdminController implements IController, Constant {
	@GET
	@Path("/workbench")
	public String workbench() {
		return jsp_perfix + "/simple_admin/workbench";
	}

	@GET
	public String admin() {
		return jsp_perfix + "/simple_admin/index";
	}
}
