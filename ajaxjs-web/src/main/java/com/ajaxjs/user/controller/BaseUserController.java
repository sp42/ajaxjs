package com.ajaxjs.user.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 基础的用户控制器，通用的會員操作，放在這裏
 * 
 * @author frank
 *
 */
public abstract class BaseUserController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserController.class);

	@Override
	public abstract UserService getService();

	/**
	 * 提示已登錄
	 */
	public static String loginedMsg = "redirect::/showMsg?msg=已经登录用户无法執行該操作";

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
//			throw new NullPointerException("ThreadLocal 未定义 HttpServletRequest。请先保存 HttpServletRequest");

		return isLogined(request);
	}

	/**
	 * 获取用户 id，在 session 中获取，也可以在 userId 请求参数中获取（前者优先）
	 * 
	 * @param request 请求对象
	 * @return Long 類型的參數
	 */
	public static Long getUserId(HttpServletRequest request) {
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

	public static Long getUserUid() {
		Long uid = null;
		HttpServletRequest request = MvcRequest.getHttpServletRequest();

		if (isLogined(request)) {
			Object obj = request.getSession().getAttribute("userUid");

			if (obj == null)
				throw new UnsupportedOperationException("Fail to access user id");
			else {
				return (Long) obj;
			}
		}

		return uid;
	}

	/**
	 * 
	 * @return
	 */
	public static Long getUserId() {
		return getUserId(MvcRequest.getHttpServletRequest());
	}

	/**
	 * 返回单个会员的详情，这会访问数据库
	 * 
	 * @return 单个会员
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

	String user(String jsp) {
		return jsp("user/" + jsp);
	}
}