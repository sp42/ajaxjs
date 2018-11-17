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
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;

@Path("/admin/feedback")
@Bean(value = "FeedbackAdminController")
public class FeedbackAdminController extends CommonController<Feedback, Long> implements CommonEntryAdminController<Feedback, Long> {

	@Resource("FeedbackService")
	private FeedbackService Service;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) throws ServiceException {
		super.list(start, limit, model);
		return jsp_perfix + "/common-entity/feedback-list";
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
	public String editUI(@PathParam("id") Long id, ModelAndView model) throws ServiceException {
		info(id, model);
		return jsp_perfix + "/common-entity/feedback";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Feedback entity, ModelAndView model) throws ServiceException {
		return super.create(entity, model);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Feedback entity, ModelAndView model) throws ServiceException {
		return super.update(id, entity, model);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) throws ServiceException {
		Feedback bean = new Feedback();
		bean.setId(id);
		return super.delete(bean, model);
	} 
}
