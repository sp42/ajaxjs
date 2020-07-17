package com.ajaxjs.mvc.controller.testcase;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.web.mvc.controller.IController;

// 测试基本的 HTTP 四个方法已经自定义流程控制
@Path("/simple")
public class SimpleController implements IController {
	@GET
	public String showHTML() {
		return "html::Hello World!";
	}

	@POST
	public String gotoJSP() {
		return "/index.jsp";
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