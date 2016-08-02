package com.ajaxjs.mvc.controller.common;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.AbstractController;

public class Read_Create_Controller<T extends BaseModel> extends AbstractController<T> {
	@GET
	@Path("/list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return "" + super.list(start, limit, model);
	}
	
	@GET
	@Override
	public String createUI(ModelAndView model) {
		return super.createUI(model);
	}
	
	@GET
	@Path("/{id}")
	@Override
	public String updateUI(@PathParam("id") long id, ModelAndView model) {
		return super.updateUI(id, model);
	}
	
	@POST
	@Override
	public String create(T entity, ModelAndView model) {
		return super.create(entity, model);
	}
	
	@PUT
	@Path("/{id}")
	@Override
	public String update(@PathParam("id") long id, T news, ModelAndView model) {
		return super.update(id, news, model);
	}
	
	@DELETE
	@Path("/{id}")
	@Override
	public String delete(@PathParam("id") long id, ModelAndView model) {
		return super.delete(id, model);
	}
}
