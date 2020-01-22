package com.ajaxjs.workflow.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.workflow.model.entity.Process;
import com.ajaxjs.workflow.service.ProcessService;

@Path("/process")
public class PController extends BaseController<Process> {
	@Resource("ProcessService")
	private ProcessService service = new ProcessService();

	@GET
	@Path("list")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		mv.put(PageResult, service.findPagedList(start, limit));
		prepareData(mv);
		return jsp("workflow/process-admin-list.jsp");
	}

	@Override
	public IBaseService<Process> getService() {
		return service;
	}
}