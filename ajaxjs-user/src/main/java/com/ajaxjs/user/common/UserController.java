package com.ajaxjs.user.common;

import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuthService;
import com.ajaxjs.user.UserService;
import com.ajaxjs.user.controller.AbstractLoginController;
import com.ajaxjs.user.controller.AbstractRegisterController;
import com.ajaxjs.user.controller.AbstractUserCenterController;
import com.ajaxjs.user.controller.LoginCheck;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.captcha.CaptchaFilter;

/**
 * 用户总的控制器
 * 
 * @author Administrator
 *
 */
@Path("/user")
@Bean
public class UserController extends AbstractUserCenterController {
	private static final LogHelper LOGGER = LogHelper.getLog(UserController.class);
	
	@Resource("User_common_authService")
	private UserCommonAuthService authService;

	@Resource("UserService")
	private UserService service;

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/center")
	public String home(ModelAndView mv, HttpServletRequest r) {
		LOGGER.info("用户会员中心（前台）");
		return jsp("user/home");
	}
	
	@GET
	@Path("/accountcenter")
	public String accountcenter() {
		LOGGER.info("用户会员中心（前台）");
		return jsp_perfix_webinf + "/user/info";
	}

	@GET
	@Path("/register")
	public String regsiter() {
		LOGGER.info("用户注册页");
		return jsp_perfix_webinf + "/user/register";
	}

	@GET
	@Path("/login")
	public String login() {
		LOGGER.info("用户登录页");
		return jsp_perfix_webinf + "/user/login";
	}


	@Override
	public UserService getService() {
		return service;
	}

	static class RegisterController extends AbstractRegisterController {
		private UserService service;

		public RegisterController(UserService service) {
			this.service = service;
		}

		@Override
		public UserService getService() {
			return service;
		}
	}

	@POST
	@Path("/register")
	@MvcFilter(filters = { DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String doRegister(User user, @NotNull @QueryParam("password") String password) throws ServiceException {
		new RegisterController(service).registerByPhone(user, password);
		return jsonOk("恭喜你，注册成功");
	}

	static class LoginController extends AbstractLoginController {
		private UserService service;

		public LoginController(UserService service) {
			this.service = service;
		}

		@Override
		public UserService getService() {
			return service;
		}

		@Override
		public BiConsumer<User, HttpServletRequest> getAfterLoginCB() {
			return null;
		}
	}

	@POST
	@Path("/login")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String loginAction(@BeanParam User user, @NotNull @QueryParam("password") String password,
			HttpServletRequest request) throws ServiceException {
		return new LoginController(service).loginAction(user, password, request);
	}
}