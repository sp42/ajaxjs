package com.ajaxjs.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.model.UserAddress;
import com.ajaxjs.user.service.UserAddressService;

/**
 * 
 * 控制器
 */
@Bean
@Path("/user/address")
public class UserAddressController extends BaseController<UserAddress> {
	@Resource("UserAddressService")
	private UserAddressService service;

	static String jsp = "user/address";

	@GET
	@Path("list")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv, HttpServletRequest r) {
		listPaged(start, limit, mv, (s, l) -> service.findPagedList(s, l, QueryParams.initSqlHandler(r)));
		return jsp(jsp);
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class })
	public String createUI() {
		return jsp(jsp + "-info");
	}
	
	@GET
	@Path("{id}")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return jsp(jsp + "-info");
	}

/*	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String createUserAddress(@NotNull @HeaderParam(USER_ID) long userId, UserAddress entity) {
		entity.setUserId(userId);
		return create(entity, service);
	}*/
	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String createUserAddress(@NotNull @BeanParam UserAddress entity) {
	
		return create(entity, service);
	}

	@PUT
	@Path("{id}")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String updateUserAddress(@PathParam("id") Long id, UserAddress entity) {
		return update(id, entity, service);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new UserAddress());
	}

	@Override
	public IBaseService<UserAddress> getService() {
		return service;
	}
}
