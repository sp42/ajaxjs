package com.ajaxjs.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.user.UserCommonAuthService;
import com.ajaxjs.user.controller.LoginLogController.UserLoginLogService;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 帐号管理控制器
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractAccountInfoController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractAccountInfoController.class);

	private UserCommonAuthService passwordService = new UserCommonAuthService();

	public UserCommonAuthService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(UserCommonAuthService passwordService) {
		this.passwordService = passwordService;
	}

	@GET
	@Path("/account")
	public String account() {
		LOGGER.info("用户会员中心-帐号管理-首页");
		return jsp("user/user-center/account");
	}

	@GET
	@Path("/account/oauth")
	public String oauth() {
		LOGGER.info("用户会员中心-账户绑定");
		return jsp("user/user-center/oauth");
	}

	public static UserLoginLogService userLoginLogService = new UserLoginLogService();

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/account/log-history")
	public String logHistory(ModelAndView mv) {
		LOGGER.info("用户会员中心-登录历史");
		long userId = getUserId();
		mv.put("list", userLoginLogService
				.findList(sql -> sql + " WHERE userId = " + userId + " ORDER BY id DESC LIMIT 0, 10"));

		return jsp("user/user-center/log-history");
	}

	@POST
	@Path("/resetPassword")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class, UserPasswordFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String resetPassword(@NotNull @QueryParam("new_password") String new_password, HttpServletRequest request)
			throws ServiceException {
		LOGGER.info("重置密码");

		if (getPasswordService() != null && getPasswordService()
				.updatePwd((UserCommonAuth) request.getAttribute("UserCommonAuthId"), new_password))
			return jsonOk("重置密码成功");
		else
			return jsonNoOk("重置密码失败！");
	}

	@POST
	@Path("/modiflyUserName")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyUserName(@NotNull @QueryParam("userName") String userName, HttpServletRequest request) {
		LOGGER.info("修改用户名");

		User user = new User();
		user.setId(getUserId(request));
		user.setName(userName);

		if (getService().update(user) != 0) {
			request.getSession().setAttribute("userName", user.getName());
			return jsonOk("修改用户名成功");
		} else
			return jsonNoOk("修改用户名失败！");
	}

	@POST
	@Path("/modiflyEmail")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyEmail(@NotNull @QueryParam("email") String email) {
		LOGGER.info("修改邮箱");

		User user = new User();
		user.setId(getUserId());
		user.setEmail(email);

		return getService().update(user) != 0 ? jsonOk("修改邮箱成功") : jsonNoOk("修改邮箱失败！");
	}

	@POST
	@Path("/modiflyPhone")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyPhone(@NotNull @QueryParam("phone") String phone) {
		LOGGER.info("修改手机");
		User user = new User();
		user.setId(getUserId());
		user.setPhone(phone);

		if (getService().update(user) != 0) {
			// request.getSession().setAttribute("userName", phone);
			return jsonOk("修改手机成功");
		} else
			return jsonNoOk("修改手机失败！");
	}
}
