package com.ajaxjs.user.controller;

import java.util.List;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.login.UserLoginLog;
import com.ajaxjs.user.service.UserDao;

/**
 * 
 * 后台查看登录日志
 */
@Path("/admin/userLoginLog")
public class LoginLogController extends BaseController<UserLoginLog> {
	@TableName(value = "user_login_log", beanClass = UserLoginLog.class)
	public static interface UserLoginLogDao extends IBaseDao<UserLoginLog> {
		@Select("SELECT e.*, user.name AS userName FROM ${tableName} e " + UserDao.LEFT_JOIN_USER + WHERE_REMARK_ORDER)
		@Override
		public PageResult<UserLoginLog> findPagedList(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT * FROM ${tableName} WHERE userId = ? ORDER BY createDate LIMIT 1")
		public UserLoginLog getLastUserLoginedInfo(long userId);
	}

	public static class UserLoginLogService extends BaseService<UserLoginLog> {
		public UserLoginLogDao dao = new Repository().bind(UserLoginLogDao.class);

		{
			setUiName("用户登录日志");
			setShortName("userLoginLog");
			setDao(dao);
		}

		public PageResult<UserLoginLog> findAll(int start, @QueryParam(limit) int limit) {
			return findPagedList(start, limit, byAny().andThen(BaseService::betweenCreateDate));
		}

		public List<UserLoginLog> findListByUserId(long userId) {
			return findList(by("userId", userId).andThen(BaseService::orderById_DESC).andThen(top(10)));
		}
	}

	public final static UserLoginLogService service = new UserLoginLogService();

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		mv.put("LoginType", UserDict.LOGIN_TYPE);
		page(mv, service.findAll(start, limit), CommonConstant.UI_ADMIN);

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
		if (request == null)
			return;

		String ip = ((MvcRequest) request).getIp();

		if ("0:0:0:0:0:0:0:1".equals(ip))
			ip = "localhost";

		bean.setIp(ip);
		bean.setUserAgent(request.getHeader("user-agent"));
		bean.setAdminLogin("true".equals(request.getParameter("isAdminLogin")));
	}
}
