package com.ajaxjs.cms.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.cms.service.FeedbackService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/admin/feedback")
@Bean
public class FeedbackAdminController extends CommonController<Feedback, Long> implements CommonEntryAdminController<Feedback, Long> {

	@Resource("FeedbackService")
	private FeedbackService service;
	
	{
		setTableName("feedback");
		setUiName("留言反馈");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		list(start, limit, model, service);
		return adminList_CMS();
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		return show405;
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/{id}")
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		editUI(id, mv, service);
		return infoUI_CMS();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Feedback entity) {
		return create(entity, service);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Feedback entity) {
		return update(id, entity, service);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id) {
		return delete(id, new Feedback(), service);
	} 
}
