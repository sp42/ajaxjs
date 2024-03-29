package com.ajaxjs.web.testcase;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.web.mvc.IController;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.MvcRequest;

@Path("/MyTopPath_And_SubPath")
public class SubPathController implements IController {
	@GET
	public String showHTML() {
		return "html::Hello World!";
	}
	
	@GET
	@Path("subPath")
	public String showHi(HttpServletRequest request, HttpServletResponse response)  {
		return "html::Hello " + request.getParameter("name");
	}

	@POST
	@Path("subPath")
	public String gotoJSP(@QueryParam("name") String name) {
		return "html::Hello " + name;
	}

	@PUT
	@Path("subPath")
	public void redirect(@QueryParam("name") String name, HttpServletResponse response) throws IOException {
		response.getWriter().print("hi," + name);
	}

	@DELETE
	@Path("subPath")
	public String showJSON(MvcRequest mvcRequest, ModelAndView mv) {
		mv.put("foo", "bar");
		return "json::{\"name\":\"Jack\"}";
	}
}