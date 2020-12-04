package com.ajaxjs.user;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.MvcRequest;

/**
 * 用户通用控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseUserController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserController.class);

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

	/**
	 * 
	 * @param jsp
	 * @return
	 */
	public String user(String jsp) {
		return jsp("user/" + jsp);
	}
}
