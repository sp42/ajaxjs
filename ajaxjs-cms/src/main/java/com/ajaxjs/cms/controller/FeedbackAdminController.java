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

import com.ajaxjs.cms.FeedbackService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/feedback")
public class FeedbackAdminController extends BaseController<EntityMap>  {

	@Resource("FeedbackService")
	private FeedbackService service;

	@GET
	@Path("/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView mv) {
		listPaged(start, limit, mv);
		return adminListCMS();
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		return show405;
	}

	@GET
	@Override
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return editUI_CMS();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(EntityMap entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, EntityMap entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new EntityMap());
	}

	@Override
	public IBaseService<EntityMap> getService() {
		return service;
	} 
}
