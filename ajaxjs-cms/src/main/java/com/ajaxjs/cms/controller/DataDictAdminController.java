package com.ajaxjs.cms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/DataDict")
public class DataDictAdminController extends BaseController<Map<String, Object>> {
	@Resource("DataDictService")
	private DataDictService service;

	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list() {
		System.out.println(service.getDao().findList().get(0).getClass());
		return toJson(service.getDao().findList());
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return editUI_CMS();
	}

	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		return show405;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		System.out.println("::::::::::::::");
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}
