package com.ajaxjs.user.login;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.BaseUserController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.UserHelper;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 注册控制器。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user/login")
@Component
public class LoginController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(LoginController.class);

	@Resource
	private LoginService service;

	@GET
	public String login(ModelAndView mv) {
		LOGGER.info("用户登录页");
		List<String> userID = new ArrayList<>(); /* 设置用户显示可选择的帐号 */

		int passWordLoginType = ConfigService.getValueAsInt("user.login.passWordLoginType");

		if (UserHelper.testBCD(UserConstant.PSW_LOGIN_ID, passWordLoginType))
			userID.add("用户名");
		if (UserHelper.testBCD(UserConstant.PSW_LOGIN_EMAIL, passWordLoginType))
			userID.add("邮件");
		if (UserHelper.testBCD(UserConstant.PSW_LOGIN_PHONE, passWordLoginType))
			userID.add("手机");

		mv.put("userID", String.join("/", userID));
		return jsp("user/login");
	}

	@GET
	@Path("/admin/login")
	public String login() {
		return jsp("admin/admin-login");
	}

	@GET
	@Path("/admin")
	public String admin() {
		return jsp("admin/admin");
	}

	@GET
	@Path("/admin/home")
	public String home() {
		return jsp("admin/home");
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
	public String loginByPassword(@NotNull @QueryParam("userID") String userID, @NotNull @QueryParam("password") String password, HttpServletRequest req)
			throws ServiceException {
		LOGGER.info("执行登录（按密码的）");

		if (isLogined())
			return jsonNoOk("你已经登录，无须重复登录！");

		User user = service.loginByPassword(userID, password, req);

		if (user != null) {
			String msg = user.getName() == null ? user.getPhone() : user.getName();
			return jsonOk("用户 " + msg + "登录成功！欢迎回来！ 三秒钟之后自动跳转。");
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
	public IBaseService<User> getService() {
		return service;
	}
}
