package com.ajaxjs.user.login;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.entity.filter.DataBaseFilter;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 后台查看登录日志
 */
@Path("/admin/user/userLoginLog")
@Component
public class LogLoginController extends BaseController<LogLogin> {
	private static final LogHelper LOGGER = LogHelper.getLog(LogLoginController.class);

	@Resource
	private LogLoginService service;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("用户登录日志-后台列表");
		mv.put("LoginType", UserConstant.LOGIN_TYPE);

		return output(mv, getService().findPagedList(start, limit), "jsp::user/login-log-list");
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
