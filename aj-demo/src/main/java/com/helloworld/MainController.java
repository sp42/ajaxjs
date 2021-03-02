package com.helloworld;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.web.mvc.IController;

@Path("/index")
public class MainController implements IController {

	@GET
	public String home() {
		return BaseController.jsp("home");
	}
}
