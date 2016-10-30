package com.ajaxjs.user.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;


import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.user.BaseUserInfoController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserService;
import com.ajaxjs.user.auth.captcha.IncorrectCaptchaException;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.web.Captcha;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

public abstract class BaseUserAccessController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserAccessController.class);
	
	public static final String request_exception_key = "exObj";

	private UserService service = new UserService();

	/**
	 * 登录界面
	 * 
	 * @return
	 */
	@GET
	@Path("/login")
	public String loginUI() {
		LOGGER.info("登录界面");
		return BaseUserInfoController.perfix + "common/user/login";
	}
	
	/**
	 * 验证码图片
	 * 
	 * @param response
	 */
	@GET
	@Path("/captchaImg")
	public void captchaImg(HttpServletRequest req, HttpServletResponse response) {
		Captcha.init(response, req.getSession());

		System.out.println(req.getSession().getAttribute(Captcha.SESSION_KEY));
	}

	@POST
	@Path("/login")
	public void loginAction(User user, HttpServletRequest request, HttpServletResponse response) {
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		Throwable exObj = (Throwable) request.getAttribute("exObj");

		String error = null;
		LOGGER.info("exceptionClassName：" + exceptionClassName);
		LOGGER.info("客户端 " + user.getName() + "登录...");

		if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
			error = "未知用户错误";
		} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
			error = "用户名/密码错误";
		} else if (IncorrectCaptchaException.class.getName().equals(exceptionClassName)) {
			error = "验证码错误";
		} else if (exObj != null && exObj.getClass().getName().equals(IllegalArgumentException.class.getName())) {
			error = exObj.getMessage(); // 缺少某个字段
		} else if (exceptionClassName != null) {
			error = "其他错误：" + exceptionClassName;
		}

		if (error == null) {
			try {
				service.afterLogin(user, new Requester(request).getIP());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		// 输出 JSON
		Responser rsp = new Responser(response);
		boolean isOk = error == null;
		String msg = error == null ? "登录成功！" : error + "具体原因：" + (exObj != null ? exObj.getMessage() : "N/A");
		rsp.outputAction(isOk, msg);
	}

	/**
	 * 登出
	 * @param request
	 */
	@Path("/logout")
	public void logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();

		if (subject != null) {
			subject.logout();
		}

		request.getSession().invalidate();
	};
}
