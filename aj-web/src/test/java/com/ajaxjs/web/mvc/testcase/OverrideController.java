package com.ajaxjs.web.mvc.testcase;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.web.mvc.controller.IController;

@Path("/OverrideTest")
public class OverrideController extends SimpleController implements IController {
	@GET
	@Override
	public String showHTML() {
		return "html::Hello World!(@Override)";
	}

	@POST
	public void gotoJSP(HttpServletResponse response) throws IOException {
		response.getWriter().print("hihi");
	}
}