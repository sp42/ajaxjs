package com.ajaxjs.web.testcase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.web.mvc.IController;

// 测试基本的 HTTP 四个方法已经自定义流程控制
@Path("/form")
public class FormController implements IController {
	@POST
	public String gotoJSP(@FormParam("username") String username) {
		return "html::" + username;
	}

	@POST
	@Path("map")
	public String map(Map<String, Object> map) {
//		int age = (int) map.get("age");
		return "html::" + map.get("username");
	}

//	@POST
//	@Path("bean")
//	public String bean(News news) {
//		return "html::" + news.getName();
//	}

	@GET
	public String get(HttpServletRequest request, HttpServletResponse response) {
		// HttpSession session = request.getSession();
		// ServletContext context = request.getServletContext();
		return "/my.jsp";
	}

	@PUT
	public String redirect() {
		return "redirect::http://blog.csdn.net/zhangxin09";
	}

	@DELETE
	public String showJSON() {
		return "json::{\"name\":\"Jack\"}";
	}
}