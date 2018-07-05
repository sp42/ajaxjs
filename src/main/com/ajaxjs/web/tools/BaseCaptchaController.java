/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.tools;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.ws.rs.GET;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Controller
public abstract class BaseCaptchaController implements IController {
	/**
	 * SESSION 的键值
	 */
	public static final String SESSION_KEY = "CaptchaSession";

	/**
	 * 表单中指定的字段名称
	 */
	public static final String submitedFieldName = "captchaImgCode";

	/**
	 * 生成验证码图片
	 * 
	 * @param req
	 *            请求对象
	 * @param response
	 *            响应对象
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
