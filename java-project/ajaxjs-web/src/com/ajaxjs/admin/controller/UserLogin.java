package com.ajaxjs.admin.controller;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.web.Captcha;
import com.ajaxjs.web.Stream;

@Controller
@Path("/admin/member")
public class UserLogin implements IController {
	private static final String perfix = "/WEB-INF/jsp/common/admin/";
	
	@GET
	@Path("/login")
	public String login(ModelAndView model) {
		return perfix + "login.jsp";
	}
	
	@POST
	@Path("/login")
	public void doLogin(@QueryParam("captchaImgCode") String captcha,
			@QueryParam("name") String name,
			@QueryParam("password") String password, HttpServletRequest request, Stream response) {
		// 临时的
		if(!"admin".equals(name) || !"admin".equals(password)) {
//			response.outputJSON("{\"isOk\":false, \"msg\": \"用户名或密码错误！\"}");
			return;
		}
		
		try {
			if(Captcha.isPass(request, captcha)){
//				response.outputJSON("{\"isOk\":true, \"msg\": \"登录成功！\"}");
			}
		} catch (Throwable e) {
//			response.outputJSON("{\"isOk\":false, \"msg\": \"" + e.getMessage() +"\"}");
		}
	}
	
	@GET
	@Path("/captchaImg")
	public void captcha(HttpServletRequest request, HttpServletResponse response) {
		Captcha.init(response, request.getSession());
	}
}
