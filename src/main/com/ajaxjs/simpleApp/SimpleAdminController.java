package com.ajaxjs.simpleApp;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;

/**
 * for demo, simple use
 * 
 * @author admin
 *
 */
@Controller
@Path("/ajaxjsweb-admin")
public class SimpleAdminController implements IController, Constant {
	@GET
	@Path("workbench")
	public String workbench() {
		return common_jsp_perfix + "simple_admin/workbench";
	}
	
	@GET
	public String admin() {
		return common_jsp_perfix + "simple_admin/index";
	}
}
