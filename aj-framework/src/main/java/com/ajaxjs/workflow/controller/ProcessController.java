package com.ajaxjs.workflow.controller;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.cache.MemoryCacheManager;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.ajaxjs.workflow.example.MyActiveProcessDao;
import com.ajaxjs.workflow.model.ToJsonHelper;
import com.ajaxjs.workflow.process.ProcessDefinition;
import com.ajaxjs.workflow.process.service.ProcessActiveService;
import com.ajaxjs.workflow.process.service.ProcessDefinitionService;

@Component
@Path("/admin/workflow/process")
public class ProcessController extends BaseController<ProcessDefinition> {
	@Resource
	private ProcessDefinitionService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(HttpServletRequest r, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(QueryTools.byAny(r)).andThen(BaseService::betweenCreateDate);

			return toJson(service.findPagedList(start, limit, handler));
		} else {
			prepareData(mv);
			return jsp("workflow/process-def");
		}
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String info(@PathParam(ID) Long id, ModelAndView mv) {
		service.setCacheManager(new MemoryCacheManager()); // 后台读取不需要缓存
		return output(mv, service.findById(id), "jsp::workflow/process-info");
	}
	
	@GET
	@Path("getJson/" + ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String getJson(@PathParam(ID) Long id, ModelAndView mv) {
		service.setCacheManager(new MemoryCacheManager()); // 后台读取不需要缓存
		ProcessDefinition def = service.findById(id);
		
		return "json::" + ToJsonHelper.getModelJson(def.getModel());
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, ProcessDefinition entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new ProcessDefinition());
	}

	@GET
	@Path("design")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String design(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("workflow/process-admin-list");
	}

	@GET
	@Path("design/" + ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String designInfo(ModelAndView mv) {
		return page("design");
	}

	@GET
	@Path("start/" + ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String start(ModelAndView mv) {
		return jsonOk("ok");
	}

	@POST
	@Path("upload")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String upload(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put(PAGE_RESULT, service.findPagedList(start, limit));
		return page("process");
	}

	@Resource
	private ProcessActiveService activeService;

	private static MyActiveProcessDao ACTIVE_PROCESS_DAO = new Repository().bind(MyActiveProcessDao.class);
	
	@GET
	@Path("active")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String active(HttpServletRequest r, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(QueryTools.byAny(r)).andThen(BaseService::betweenCreateDate);

			return toJson(ACTIVE_PROCESS_DAO.findAdminList(start, limit, handler));
		} else {
			mv.put("uiName", "流程实例");
			mv.put("shortName", "process-active");
			mv.put("tableName", activeService.getTableName());
			return page("process-active");
		}
	}

	@Override
	public IBaseService<ProcessDefinition> getService() {
		return service;
	}

}
