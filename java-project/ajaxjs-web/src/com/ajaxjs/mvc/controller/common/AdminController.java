package com.ajaxjs.mvc.controller.common;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.AbstractController;

public abstract class AdminController<T extends BaseModel> extends AbstractController<T> {
	static final String perfix = "/WEB-INF/jsp/entry/";
 
	
	@GET
	@Override
	public String createUI(ModelAndView model) {
		return perfix + super.createUI(model) + "/adminInfo.jsp";
	}
	
	@GET
	@Path("/{id}")
	@Override
	public String updateUI(@PathParam("id") long id, ModelAndView model) {
		return perfix + super.updateUI(id, model) + "/adminInfo.jsp";
	}
	
//	@POST
//	@Override
//	public abstract String create(T entity, ModelAndView model);
//	
//	@PUT
//	@Path("/{id}")
//	@Override
//	public abstract String update(@PathParam("id") long id, T entry, ModelAndView model);
}
