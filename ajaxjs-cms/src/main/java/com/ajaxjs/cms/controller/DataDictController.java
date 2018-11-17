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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.service.DataDictService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;

@Path("/admin/DataDict")
@Bean("DataDictAdminController")
public class DataDictController extends CommonController<Map<String, Object>, Integer> implements CommonEntryAdminController<Map<String, Object>, Integer> {
	@Resource("DataDictService")
	private DataDictService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return listJson(start, limit, model);
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		model.put("uiName", getService().getName());
		return jsp_perfix + "/system/Datadict";
	}

	@Override
	public String editUI(@PathParam("id") Integer id, ModelAndView model) {
		return show405;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity, ModelAndView model) {
		return super.create(entity, model);
	}

	@PUT
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Integer id, Map<String, Object> entity, ModelAndView model) {
		return super.update(id, entity, model);
	}

	@DELETE
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Integer id, ModelAndView mv) {
		return delete(id, new HashMap<String, Object>(), mv, entry -> service.delete(entry));
	}
}
