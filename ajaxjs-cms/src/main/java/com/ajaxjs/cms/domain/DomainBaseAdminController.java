package com.ajaxjs.cms.domain;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

public abstract class DomainBaseAdminController extends BaseController<DomainEntity> {
	@GET
	@Override
	@MvcFilter(filters = DataBaseFilter.class)
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return domainEntityEdit;
	}

	@GET
	@Override
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return domainEntityEdit;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(DomainEntity entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, DomainEntity entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new DomainEntity());
	}
}
