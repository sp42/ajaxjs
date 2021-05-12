package com.ajaxjs.workflow.controller;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.ajaxjs.workflow.task.Task;
import com.ajaxjs.workflow.task.service.TaskService;

@Component
@Path("/admin/workflow/task")
public class TaskController extends BaseController<Task> {
	@Resource
	private TaskService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(HttpServletRequest r, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(QueryTools.byAny(r)).andThen(BaseService::betweenCreateDate);

			return toJson(service.findPagedList(start, limit, handler));
		} else {
			prepareData(mv);
			return page("workflow/task");
		}
	}

	@GET
	@Path("help")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String help(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("workflow/process-admin-list");
	}
	
	@GET
	@Path("ccorder")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String ccorder(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("workflow/process-admin-list");
	}
	
	@GET
	@Path("history")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String history(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("workflow/process-admin-list");
	}
	
	@POST
	@Path("upload")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String upload(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("workflow/process-admin-list");
	}
	
	@GET
	@Path("active")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String active(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		mv.put("uiName", "流程实例");
		mv.put("shortName", "process-active");
		mv.put("tableName", service.getTableName());

		return page("workflow/process-admin-list");
	}

	@Override
	public IBaseService<Task> getService() {
		return service;
	}

}
