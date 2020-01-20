package com.ajaxjs.workflow.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.workflow.service.ProcessService;

@Path("/process")
public class PController extends BaseController<Map<String, Object>> {
	@Resource("ProcessService")
	private ProcessService service = new ProcessService();

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	@GET
	@Path("list")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		mv.put(PageResult, service.findPagedList(start, limit));
		prepareData(mv);
		return jsp("workflow/process-admin-list.jsp");
	}
}
