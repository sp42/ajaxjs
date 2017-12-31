package com.ajaxjs.simpleApp.controller;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.simpleApp.service.CatalogService;
import com.ajaxjs.simpleApp.service.HrService;
import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryReadOnlyController;

@Controller
@Path("/hr")
@Bean("HrController")
public class HrController extends CommonController<Map<String, Object>, Long, HrService> implements CommonEntryReadOnlyController {

	@Resource("HrService")
	private HrService service;

	@Resource("CatalogService")
	private CatalogService catalogService;

	public CatalogService getCatalogService() {
		return catalogService;
	}

	public void setCatalogService(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	@GET
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		initDb();
		model.put("catalogMenu", getService().getCatalog());
		super.pageList(start, limit, model);
		return String.format(jsp_list, getService().getTableName());
	}

	@GET
	@Override
	@Path("{id}")
	public String info(@PathParam("id") Long id, ModelAndView model) {
		initDb();
		model.put("catalogMenu", catalogService.findAll(MvcRequest.factory()));
		return super.info(id, model);
	}
}
