package com.ajaxjs.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.User;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;

@Path("/user/info")
@Bean("PcUserInfoController")
public class UserInfoController extends AbstractInfoController {

	@Resource("UserService")
	private UserService service;

	@Resource("User_common_authService") // 指定 service id
	private UserCommonAuthService passwordService;

	@PUT
	@Path("/{id}")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Override
	public String update(@PathParam("id") Long id, User entity) {
		return super.update(id, entity);
	}

	@POST
	@Path("/avatar")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String updateOrCreateAvatar(MvcRequest request) throws Exception {
		return super.updateOrCreateAvatar(request);
	}

	@POST
	@Path("/resetPassword")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class, UserPasswordFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String resetPassword(@NotNull @QueryParam("new_password") String new_password, HttpServletRequest request) throws ServiceException {
		return super.resetPassword(new_password, request);
	}

	@POST
	@Path("/modiflyUserName")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String modiflyUserName(@NotNull @QueryParam("userName") String userName, HttpServletRequest request) {
		return super.modiflyUserName(userName, request);
	}

	@POST
	@Path("/modiflyEmail")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String modiflyEmail(@NotNull @QueryParam("email") String email) {
		return super.modiflyEmail(email);
	}

	@POST
	@Path("/modiflyPhone")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String modiflyPhone(@NotNull @QueryParam("phone") String phone) {
		return super.modiflyPhone(phone);
	}

	@Override
	public UserService getService() {
		return service;
	}
}
