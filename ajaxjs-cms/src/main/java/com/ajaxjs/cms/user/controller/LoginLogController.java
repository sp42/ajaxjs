package com.ajaxjs.cms.user.controller;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.user.UserDict;
import com.ajaxjs.cms.user.UserLoginLog;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

/**
 * 
 * 登录日志
 */
@Path("/admin/userLoginLog")
public class LoginLogController extends BaseController<UserLoginLog> {
	@TableName(value = "user_login_log", beanClass = UserLoginLog.class)
	public static interface UserLoginLogDao extends IBaseDao<UserLoginLog> {
		@Select("SELECT e.*, user.name AS userName FROM ${tableName} e LEFT JOIN user ON user.id = e.userId WHERE 1 = 1 ORDER BY e.id DESC")
		@Override
		public PageResult<UserLoginLog> findPagedList(int start, int limit, Function<String, String> sqlHandler);
	}

	public static class UserLoginLogService extends BaseService<UserLoginLog> {
		public UserLoginLogDao dao = new Repository().bind(UserLoginLogDao.class);

		{
			setUiName("用户登录日志");
			setShortName("userLoginLog");
			setDao(dao);
		}
	}

	public static UserLoginLogService service = new UserLoginLogService();

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv, HttpServletRequest req) {
		mv.put("LoginType", UserDict.LoginType);
		listPaged(start, limit, mv, (s, l) -> service.findPagedList(s, l, QueryParams.initSqlHandler(req)));
		
		return jsp("user/login-log-list");
	}

	@Override
	public IBaseService<UserLoginLog> getService() {
		return service;
	}

	/**
	 * 获取客户端有关信息
	 * 
	 * @param bean
	 * @param request
	 */
	public static void initBean(UserLoginLog bean, HttpServletRequest request) {
		String ip = ((MvcRequest) request).getIp();
		if ("0:0:0:0:0:0:0:1".equals(ip))
			ip = "localhost";

		bean.setIp(ip);
		bean.setUserAgent(request.getHeader("user-agent"));
		bean.setAdminLogin("true".equals(request.getParameter("isAdminLogin")));
	}
}
