package com.ajaxjs.user.login;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.User;
import com.ajaxjs.user.profile.ProfileService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 注册控制器。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user/login")
@Component
public class LoginController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(LoginController.class);

	@Resource
	private ProfileService service;

	@GET
	public String login(ModelAndView mv) {
		LOGGER.info("用户登录页");
		LoginService.setUserId(mv);
		return jsp("user/login");
	}

	@GET
	@Path("isLogin")
	public String isLogin() {
		LOGGER.info("查看用户是否已经登陆");
		return toJson(isLogined());
	}

	@POST
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String loginByPassword(@NotNull @QueryParam("userID") String userID,
			@NotNull @QueryParam("password") String password, HttpServletRequest req) throws ServiceException {
		LOGGER.info("执行登录（按密码的）");

		if (isLogined())
			return jsonNoOk("你已经登录，无须重复登录！");

		userID = userID.trim();
		User user = LoginService.loginByPassword(userID, password);

		if (user != null) {
			LoginService.saveLoginLog(user, req);

//			if (getAfterLoginCB() != null)
//				getAfterLoginCB().accept(user, req);

			LoginService.afterLogin(user, req);

			String msg = user.getName() == null ? user.getPhone() : user.getName();

			return jsonOk("用户 " + msg + "登录成功！欢迎回来！ <a href=\"" + req.getContextPath() + "/user/\">点击进入“用户中心”</a>。");
		} else
			return jsonNoOk("登录失败！");
	}

	@GET
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String doLogout(HttpServletRequest req) {
		LOGGER.info("用户登出");

		req.getSession().invalidate();
		return jsonOk("退出成功！");
	}

	/**
	 * 会员是否已经登录
	 * 
	 * @param request 请求对象
	 * @return true=會員已經登錄；fasle=未登录
	 */
	public static boolean isLogined(HttpServletRequest request) {
		return request != null && request.getSession() != null && request.getSession().getAttribute("userId") != null;
	}

	/**
	 * 会员是否已经登录。该方法不用传入请求对象，而是从 ThreadLocal 获取，使用更简单。
	 * 
	 * @return true=會員已經登錄；fasle=未登录
	 */
	public static boolean isLogined() {
		HttpServletRequest request = MvcRequest.getHttpServletRequest();

		if (request == null)
			return false;

		return isLogined(request);
	}

	/**
	 * 获取用户 id，在 session 中获取，也可以在 userId 请求参数中获取（前者优先）
	 * 
	 * @param request 请求对象
	 * @return 用户 id
	 */
	public static long getUserId(HttpServletRequest request) {
		if (isLogined(request)) {
			Object obj = request.getSession().getAttribute("userId");

			if (obj instanceof Integer)
				return ((Integer) obj).longValue();
			else if (obj instanceof Long)
				return (Long) obj;
			else if (obj instanceof String)
				return Long.parseLong((String) obj);
			else
				throw new UnsupportedOperationException("Unknow type! " + obj);

		} else if (request.getParameter("userId") != null)
			return Long.parseLong(request.getParameter("userId"));
		else if (!CommonUtil.isEmptyString(request.getHeader(USER_ID_HEADER)))
			return Long.parseLong(request.getHeader(USER_ID_HEADER));
		else
			return 0L;
//			throw new UnsupportedOperationException("Fail to access user id");
	}

	/**
	 * 获取用户 id，在 session 中获取，也可以在 userId 请求参数中获取（前者优先）
	 * 
	 * @return 用户 id
	 */
	public static long getUserId() {
		return getUserId(MvcRequest.getHttpServletRequest());
	}

	/**
	 * 获取用户 uid
	 * 
	 * @return 用户 uid
	 */
	public static Long getUserUid() {
		Long uid = null;
		HttpServletRequest request = MvcRequest.getHttpServletRequest();

		if (isLogined(request)) {
			Object obj = request.getSession().getAttribute("userUid");

			if (obj == null)
				throw new UnsupportedOperationException("Fail to access user id");
			else
				return (Long) obj;
		}

		return uid;
	}

	/**
	 * 返回单个会员的详情，这会访问数据库
	 * 
	 * @return 用户对象
	 */
	public User getUserDetail() {
		try {
			return getService().findById(getUserId());
		} catch (Throwable e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 返回用户对象，这通过 session 返回信息，不会太多信息但是速度比较快（不用访问数据库）
	 * 
	 * @return 用户对象
	 */
	public User getUserBySession() {
		User user = new User();
		user.setId(getUserId());
		HttpServletRequest request = MvcRequest.getHttpServletRequest();

		if (request.getSession().getAttribute("userName") != null)
			user.setName(request.getSession().getAttribute("userName").toString());

		if (request.getSession().getAttribute("userPhone") != null)
			user.setPhone(request.getSession().getAttribute("userPhone").toString());

		return user;
	}

	public String user(String jsp) {
		return jsp("user/" + jsp);
	}

	@Override
	public IBaseService<User> getService() {
		return service;
	}
}
