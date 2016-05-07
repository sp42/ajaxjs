package com.ajaxjs.framework.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.user.captcha.IncorrectCaptchaException;
import com.ajaxjs.web.Captcha;
import com.ajaxjs.web.IP;
import com.ajaxjs.web.Responser;


@Controller
@RequestMapping(value = "/service/user/access")
public class UserAccessController {
	private static final com.ajaxjs.util.LogHelper LOGGER = com.ajaxjs.util.LogHelper.getLog(UserAccessController.class);
	
	private Service service = new Service();
	
	/**
	 * 登录界面
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginUI() {
		LOGGER.info("登录界面");
		return "common/user/login";
	}
	
	/**
	 * 验证码图片
	 * @param response
	 */
	@RequestMapping(value = "/captchaImg", method = RequestMethod.GET)
	public void captchaImg(HttpServletRequest req, HttpServletResponse response) {
		Captcha.init(response, req.getSession());
		
		System.out.println(req.getSession().getAttribute(Captcha.SESSION_KEY));
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void loginAction(User user, HttpServletRequest request, HttpServletResponse response) {
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		Throwable exObj = (Throwable) request.getAttribute("exObj");

		String error = null;
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
		
		if(error == null){
			try {
				service.afterLogin(user, IP.getIP(request));
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

	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();

		if (subject != null) {
			subject.logout();
		}

		request.getSession().invalidate();

	};
}
