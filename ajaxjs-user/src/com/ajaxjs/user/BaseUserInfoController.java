package com.ajaxjs.user;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.user.auth.captcha.IncorrectCaptchaException;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.web.Captcha;

@Controller
@Path("/user")
public class BaseUserInfoController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserInfoController.class);
	
	public static final String perfix = "/asset/jsp/";
	
//	private Service service = new Service();
	
	/**
	 * 显示修改用户信息界面
	 * @return
	 */
	@GET
	public String loginUI() {
		LOGGER.info("profile界面");
		return perfix + "user/login";
	}
	
	/**
	 * 显示修改用户信息界面
	 * @return
	 */
	@GET
	@Path("/profile")
	public String profileUI() {
		LOGGER.info("profile界面");
		return perfix + "user/profile";
	}
	
	/**
	 * 修改用户信息
	 * @return
	 */
	@POST
	@Path("/profile")
	public String updateProfile() {
		LOGGER.info("updateProfile");
		return perfix + "user/login";
	}
	
	/**
	 * 显示修改密码界面
	 * @return
	 */
	@GET
	@Path("/updatePassword")
	public String updatePasswordUI() {
		LOGGER.info("登录信息界面");
		return perfix + "user/updatePassword";
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@POST
	@Path("/updatePassword")
	public String updatePassword() {
		LOGGER.info("修改登录信息");
		return perfix + "user/login";
	}
	
	/**
	 * 验证码图片
	 * 
	 * @param response
	 */
	@GET
	@Path(value = "/captchaImg")
	public void captchaImg(HttpServletRequest req, HttpServletResponse response) {
		Captcha.init(response, req.getSession());

		System.out.println(req.getSession().getAttribute(Captcha.SESSION_KEY));
	}
	
	public static String request_exception_key = "";

	@POST
	@Path("/login")
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

//		if (error == null) {
//			try {
//				service.afterLogin(user, new Requester(request).getIP());
//			} catch (ServiceException e) {
//				e.printStackTrace();
//			}
//		}
//
//		// 输出 JSON
//		Responser rsp = new Responser(response);
//		boolean isOk = error == null;
//		String msg = error == null ? "登录成功！" : error + "具体原因：" + (exObj != null ? exObj.getMessage() : "N/A");
//		rsp.outputAction(isOk, msg);
		
		
	}

	/**
	 * 登出
	 * @param request
	 */
	@GET
	@Path(value = "/logout")
	public void logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();

		if (subject != null) {
			subject.logout();
		}

		request.getSession().invalidate();
	}
}
