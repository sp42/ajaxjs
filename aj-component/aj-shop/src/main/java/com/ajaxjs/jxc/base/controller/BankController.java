package com.ajaxjs.jxc.base.controller;

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
import com.ajaxjs.jxc.base.model.Bank;
import com.ajaxjs.jxc.base.service.BankService;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Component
@Path("/admin/jxc/base/bank")
public class BankController extends BaseController<Bank> {
	@Resource
	private BankService service;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String get(HttpServletRequest r, @QueryParam("owner") long owner, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		if (isJson()) {
			Function<String, String> handler = BaseService::searchQuery_NameOnly;
			handler = handler.andThen(QueryTools.byAny(r)).andThen(BaseService::betweenCreateDate);

			if (owner != 0L)
				handler = handler.andThen(QueryTools.by("owner", owner));

			return toJson(service.findPagedList(start, limit, handler));
		} else {
			prepareData(mv);

			return jsp("jxc/base/bank");
		}
	}

	@GET
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String info(@PathParam(ID) long id) {
		return toJson(service.findById(id));
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Bank entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Bank entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Bank());
	}

	@Override
	public IBaseService<Bank> getService() {
		return service;
	}
}
