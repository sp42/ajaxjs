package com.ajaxjs.user.login;

import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 注册控制器。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LoginController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(LoginController.class);

	@GET
	@Path("login")
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
	@Path("login")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String loginByPassword(@NotNull @QueryParam("userID") String userID, @NotNull @QueryParam("password") String password, HttpServletRequest req) throws ServiceException {
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

	@Override
	public UserService getService() {
		// TODO Auto-generated method stub
		return null;
	}
 
}
