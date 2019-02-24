package com.ajaxjs.web.captcha;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.ajaxjs.mvc.controller.MvcOutput;

/**
 * 簡易的圖片驗證碼
 */
@WebServlet("/Captcha")
public class CaptchaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * SESSION 的键值
	 */
	public static final String SESSION_KEY = "CaptchaSession";

	/**
	 * 表单中指定的字段名称
	 */
	public static final String submitedFieldName = "captchaImgCode";

	/**
	 * GET 請求獲取驗證碼圖片
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		init(response, request.getSession());
	}

	/**
	 * 显示验证码图片并将认证码存入 Session
	 * 
	 * @param response 响应对象
	 * @param session 会话对象
	 */
	public static void init(HttpServletResponse response, HttpSession session) {
		Captcha captcha = new Captcha();
		captcha.setWidth(60).setHeight(20);
		captcha.get();

		new MvcOutput(response).noCache().setContent_Type("image/jpeg").go(captcha.getbImg());

		session.setAttribute(SESSION_KEY, captcha.getCode());
	}

	/**
	 * 显示验证码图片并将认证码存入 Session JSP 调用方式：
	 * <%com.ajaxjs.web.captcha.CaptchaController.init(pageContext);%>
	 * 
	 * @param pageContext 页面上下文对象
	 */
	public static void init(PageContext pageContext) {
		init((HttpServletResponse) pageContext.getResponse(), pageContext.getSession());
	}
}
