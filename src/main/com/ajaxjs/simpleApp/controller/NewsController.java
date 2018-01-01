package com.ajaxjs.simpleApp.controller;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.plugin.Neighbor;
import com.ajaxjs.simpleApp.service.NewsService;
import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryReadOnlyController;

@Controller
@Path("/news")
@Bean("NewsController")
public class NewsController extends CommonController<Map<String, Object>, Long, NewsService> implements CommonEntryReadOnlyController {
	@Resource("NewsService")
	private NewsService service;

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
		model.put("catalogMenu", getService().getCatalog());
		
		Neighbor.init(model, getService().getTableName(), id);
		
		return super.info(id, model);
	}
}
