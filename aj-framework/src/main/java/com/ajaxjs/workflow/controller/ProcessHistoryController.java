package com.ajaxjs.workflow.controller;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.ajaxjs.workflow.process.ProcessHistory;
import com.ajaxjs.workflow.process.service.ProcessHistoryService;

@Component
@Path("/admin/workflow/process/history")
public class ProcessHistoryController extends BaseController<ProcessHistory> {
	@Resource
	private ProcessHistoryService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(HttpServletRequest r, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(QueryTools.byAny(r)).andThen(BaseService::betweenCreateDate);

			return toJson(service.findPagedList(start, limit, handler));
		} else {
			prepareData(mv);
			return page("workflow/process-history");
		}
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String info(@PathParam(ID) Long id, ModelAndView mv) {
		return output(mv, service.findById(id), "jsp::pages/workflow/process-info");
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new ProcessHistory());
	}

	@Override
	public ProcessHistoryService getService() {
		return service;
	}

}
