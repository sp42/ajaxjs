package com.ajaxjs.web.mvc.testcase;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.web.mvc.IController;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.MvcRequest;

@Path("/MyTopPath_And_Sub_And_ID_Path")
public class SubPath_Id_Controller implements IController {
	@GET
	public String showHTML() {
		return "html::Hello World!";
	}
	
	@GET
	@Path("subPath")
	public String showHi(HttpServletRequest request, HttpServletResponse response)  {
		return "html::Hello " + request.getParameter("name");
	}
	
	@GET
	@Path("{id}")
	public String showID(HttpServletRequest request, HttpServletResponse response, @PathParam("id") int id)  {
		return "html::showID: " + id + "," + request.getParameter("name");
	}

	@POST
	@Path("{id}")
	public String gotoJSP(@PathParam("id") int id) {
		return "html::ID:" + id;
	}

	@PUT
	@Path("{id}")
	public void getId(@PathParam("id") int id, HttpServletResponse response) throws IOException {
		response.getWriter().print("ID:" + id);
	}

	@DELETE
	@Path("{id}")
	public String showJSON(MvcRequest mvcRequest, ModelAndView mv) {
		mv.put("foo", "bar");
		return "json::{\"showJSON\":\"Jack\"}";
	}
	
	@GET
	@Path("subPath/{id}")
	public String show_shuPath_ID(HttpServletRequest request, HttpServletResponse response, @PathParam("id") int id)  {
		return "html::show_shuPath_ID: " + id + "," + request.getParameter("name");
	}
}