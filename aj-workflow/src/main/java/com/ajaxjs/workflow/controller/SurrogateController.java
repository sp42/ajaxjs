package com.ajaxjs.workflow.controller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.ajaxjs.workflow.util.surrogate.Surrogate;
import com.ajaxjs.workflow.util.surrogate.SurrogateService;

@Component
@Path("/admin/workflow/surrogate")
public class SurrogateController extends BaseController<Surrogate> {
	@Resource
	private SurrogateService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		prepareData(mv);
		return page("process-admin-list");
	}

	@GET
	@Path("design")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String design(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("process-admin-list");
	}
	
	@POST
	@Path("upload")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String upload(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("process-admin-list");
	}
	
	@GET
	@Path("active")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String active(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		mv.put("uiName", "流程实例");
		mv.put("shortName", "process-active");
		mv.put("tableName", service.getTableName());

		return page("process-admin-list");
	}

	@Override
	public IBaseService<Surrogate> getService() {
		return service;
	}

}
