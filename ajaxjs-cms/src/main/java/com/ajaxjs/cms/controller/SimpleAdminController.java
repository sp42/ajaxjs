package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;

/**
 * for simple use
 *
 */
@Path("/admin")
public class SimpleAdminController implements IController {
	@GET
	public String admin() {
		return Constant.cms("admin");
	}

	@GET
	@Path("/workbench")
	public String workbench() {
		return Constant.cms("admin-workbench");
	}
}
