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
import com.ajaxjs.simpleApp.service.CatalogServiceImpl;
import com.ajaxjs.simpleApp.service.ProductService;
import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryReadOnlyController;

@Controller
@Path("/product")
@Bean("ProductController")
public class ProductController extends CommonController<Map<String, Object>, Long, ProductService> implements CommonEntryReadOnlyController {
	CatalogService catalogService = new CatalogServiceImpl();

	public ProductController() {
		setService(service);
	}
	
	@Resource("ProductService")
	private ProductService service;

	@GET
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		initDb();
		model.put("catalogMenu", catalogService.findAll(MvcRequest.factory()));
		super.pageList(start, limit, model);
		return String.format(jsp_list, service.getTableName());
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
