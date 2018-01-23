package com.ajaxjs.simpleApp.controller;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.service.DataDictService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryAdminController;

@Controller
@Path("/admin/DataDict")
@Bean("DataDictAdminController")
public class DataDictAdminController extends CommonController<Map<String, Object>, Integer, DataDictService> implements CommonEntryAdminController<Map<String, Object>, Integer> {
	@Resource("DataDictService")
	private DataDictService service;

	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		PageResult<Map<String, Object>> p = super.pageList(start, limit, model);
		return outputPagedJsonList(p, model);
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		return common_jsp_perfix + "simple_admin/edit-cataory";
	}

	@GET
	@Path("{id}")
	@Override
	public String editUI(@PathParam("id") Integer id, ModelAndView model) {
		return String.format(jsp_adminInfo, getService().getTableName());
	}

	@POST
	@Override
	public String create(Map<String, Object> entity, ModelAndView model) {
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(Map<String, Object> entity, ModelAndView model) {
		return super.update(entity, model);
	}

	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") Integer id, ModelAndView model) {
		return super.delete(id, model);
	}

	@GET
	@Path("catalog")
	public String newsCatalogUI() {
		return common_jsp_perfix + "simple_admin/edit-cataory";
	}

	@Override
	public void prepareData(ModelAndView model) {
		super.prepareData(model);
		initDb();
	}
}
