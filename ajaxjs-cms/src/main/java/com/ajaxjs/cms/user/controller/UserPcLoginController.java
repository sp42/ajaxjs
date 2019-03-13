package com.ajaxjs.cms.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.user.User;
import com.ajaxjs.cms.user.controller.AbstractLoginController;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.captcha.CaptchaFilter;

/**
 * 用户注册控制器
 * 
 * @author Sp42 frank@ajaxjs.com
 */
@Path("/user/login")
@Bean("UserPcLoginController")
public class UserPcLoginController extends AbstractLoginController {
	private static final LogHelper LOGGER = LogHelper.getLog(UserPcLoginController.class);
	
	@Resource("UserService") // 指定 service id
	private UserService service;

	@Resource("AliyunSMSSender")
	private SMS sms;

	@GET
	public String login(@QueryParam("bySMS") boolean bySMS) {
		LOGGER.info("进入登录页面");
		return jsp("user/login");
	}

	@POST
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String loginAction(@BeanParam User user,  @NotNull  @QueryParam("password") String password, HttpServletRequest request) throws ServiceException {
		String msg = loginByPassword(user, password, request);

		if (msg.equals(LOGIN_PASSED)) {
			String name = getUserBySession().getName();
			if (name == null)
				name = getUserBySession().getPhone();

			msg = "用户 " + name + " 欢迎回来！ <a href=\"" + request.getContextPath() + "/user/center/\">点击进入“用户中心”</a>";
		}

		return jsonOk("登录成功");
	}

	@GET
	@Path("/logout")
	public String doLogout() {
		super.logout();
		return "/user/index.jsp?msg=" + Encode.urlEncode("退出成功！");
	}

	@POST
	@Path("sendSMScode")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendSMScode(@NotNull @QueryParam("phoneNo") String phoneNo) {
		return super.sendSMScode(phoneNo);
	}

	@Override
	public UserService getService() {
		return service;
	}
}
