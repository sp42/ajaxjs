package com.ajaxjs.user.login;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.service.UserDao;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 后台查看登录日志
 */
@Path("/admin/userLoginLog")
public class LogLoginController extends BaseController<LogLogin> {
	private static final LogHelper LOGGER = LogHelper.getLog(LogLoginController.class);

	@TableName(value = "user_login_log", beanClass = LogLogin.class)
	public static interface UserLoginLogDao extends IBaseDao<LogLogin> {
		@Select(UserDao.LEFT_JOIN_USER_SELECT)
		@Override
		public PageResult<LogLogin> findPagedList(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT * FROM ${tableName} WHERE userId = ? ORDER BY createDate LIMIT 1")
		public LogLogin getLastUserLoginedInfo(long userId);
	}

	public static class UserLoginLogService extends BaseService<LogLogin> {
		public UserLoginLogDao dao = new Repository().bind(UserLoginLogDao.class);

		{
			setUiName("用户登录日志");
			setShortName("userLoginLog");
			setDao(dao);
		}
		
		/**
		 * 查找登录日志的列表
		 * 
		 * @param start 分页起始
		 * @param limit 分页结束
		 * @return 登录日志的列表
		 */
		public PageResult<LogLogin> findPagedList(int start, int limit) {
			return findPagedList(start, limit, byAny().andThen(BaseService::betweenCreateDateWithE).andThen(UserService.byUserId));
		}
		
		/**
		 * 根据用户 id 查找其登录日志的列表
		 * 
		 * @param userId 用户 id
		 * @return 登录日志的列表
		 */
		public List<LogLogin> findListByUserId(long userId) {
			return findList(by("userId", userId).andThen(BaseService::orderById_DESC).andThen(top(10)));
		}
	}

	public final static UserLoginLogService service = new UserLoginLogService();

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("用户登录日志-后台列表");
		mv.put("LoginType", UserConstant.LOGIN_TYPE);

		return output(mv, service.findPagedList(start, limit), "jsp::user/login-log-list");
	}

	@Override
	public IBaseService<LogLogin> getService() {
		return service;
	}

	/**
	 * 获取客户端有关信息
	 * 
	 * @param bean
	 * @param request
	 */
	public static void initBean(LogLogin bean, HttpServletRequest request) {
		if (request == null)
			return;

		String ip = ((MvcRequest) request).getIp();

		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "localhost";
			bean.setIpLocation("本机");
		} else {
			try {
				bean.setIpLocation(Tools.getIpLocation2(ip));
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		}

		bean.setIp(ip);
		bean.setUserAgent(request.getHeader("user-agent"));
		bean.setAdminLogin("true".equals(request.getParameter("isAdminLogin")));
	}
}
