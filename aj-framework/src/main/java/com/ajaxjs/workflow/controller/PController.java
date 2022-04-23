//package com.ajaxjs.workflow.controller;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.QueryParam;
//
//import com.ajaxjs.framework.BaseController;
//import com.ajaxjs.framework.filter.DataBaseFilter;
//import com.ajaxjs.sql.orm.IBaseService;
//import com.ajaxjs.util.ioc.Resource;
//import com.ajaxjs.web.mvc.ModelAndView;
//import com.ajaxjs.web.mvc.filter.MvcFilter;
//import com.ajaxjs.workflow.model.entity.Process;
//import com.ajaxjs.workflow.service.ProcessService;
//
//
//
//@Path("/process")
//public class PController extends BaseController<Process> {
//	@Resource("ProcessService")
//	private ProcessService service = new ProcessService();
//
//	@GET
//	@Path("list")
//	@MvcFilter(filters = { DataBaseFilter.class })
//	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
//		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
//		prepareData(mv);
//		return jsp("workflow/process-admin-list.jsp");
//	}
//
//	@Override
//	public IBaseService<Process> getService() {
//		return service;
//	}
//}
