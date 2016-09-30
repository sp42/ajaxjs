package com.ajaxjs.mvc.controller.common;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.AbstractController;

public class ReadOnlyController<T extends BaseModel> extends AbstractController<T> {
	@GET
	@Path("/list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return AdminController.perfix + super.list(start, limit, model) + "/frontEnd_list";
	}
	
	@GET
	@Path("/{id}")
	@Override
	public String updateUI(@PathParam("id") long id, ModelAndView model) {
		System.out.println("id:::" + id);
		return AdminController.perfix + super.updateUI(id, model) + "/frontEnd_info";
	}
}
