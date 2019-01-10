package com.ajaxjs.cms.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/userLoginLog")
public class UserLoginLogAdminController extends BaseController<UserLoginLog> {
	@Resource("UserLoginLogService")
	private UserLoginLogService service;

	@GET
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@PathParam("id") Long userId, @QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView mv) {
		mv.put("LoginType", UserDict.LoginType);
		listPaged(start, limit, mv, (s, l) -> service.dao.findUserLoginLogByUserId(userId, s, l));
		return adminListCMS();
	}


	@Override
	public IBaseService<UserLoginLog> getService() {
		return service;
	}
}
