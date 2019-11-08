package com.ajaxjs.cms.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.ajaxjs.cms.FeedbackService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/feedback")
public class FeedbackController extends BaseController<Map<String, Object>> {

	@Resource("FeedbackService")
	private FeedbackService service;

	@GET
	@Path("/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv, HttpServletRequest r) {
		listPaged(start, limit, mv, (s, l) -> service.findPagedList(s, l, QueryParams.initSqlHandler(r)));
		return adminList();
	}

	@GET
	@Override
	@Path("/feedback")
	public String createUI(ModelAndView model) {
		return show405;
	}

	@POST
	@Path("/feedback")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		// 外界用户提交反馈，可能是游客
		return super.create(entity);
	}

	@GET
	@Override
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return editUI();
	}


	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}