package com.ajaxjs.simpleApp.controller;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;

/**
 * 显示验证码控制器
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
@Controller
@Path("/showCaptchaImg")
public class CaptchaImgController implements IController {
	/**
	 * SESSION 的键值
	 */
	public static final String SESSION_KEY = "CaptchaSession";
	
	public static final String submitedFieldName = "captchaImgCode";

	/**
	 * 验证码图片
	 * 
	 * @param req
	 * @param response
	 */
	@GET
	public void captchaImg(HttpServletRequest req, HttpServletResponse response) {
		init(response, req.getSession());
	}

	/**
	 * 显示验证码图片并将认证码存入 Session
	 * 
	 * @param response
	 *            响应对象
	 * @param session
	 *            会话对象
	 */
	public static void init(HttpServletResponse response, HttpSession session) {
		Captcha captcha = new Captcha();
		captcha.setWidth(60).setHeight(20);
		captcha.get();

		new MvcOutput(response).noCache().setContent_Type("image/jpeg").go(captcha.getbImg());

		session.setAttribute(SESSION_KEY, captcha.getCode());
	}

	/**
	 * 显示验证码图片并将认证码存入 Session（For JSP） JSP 调用方式：
	 * <%com.ajaxjs.bigfoot.tools.VcodeImg.init(pageContext);%>
	 * 
	 * @param pageContext
	 *            页面上下文对象
	 */
	public static void init(PageContext pageContext) {
		init((HttpServletResponse) pageContext.getResponse(), pageContext.getSession());
	}
}
