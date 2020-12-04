package com.ajaxjs.web.mvc;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.web.mvc.controller.IController;
import com.ajaxjs.web.mvc.filter.MvcFilter;


// 测试基本的 HTTP 四个方法已经自定义流程控制
@Path("/filter")
public class FilterController implements IController {
	@GET
	@MvcFilter(filters = Filter.class)
	public String showHTML() {
		return "html::Hello World!";
	}
	
	@GET
	@Path("captcha")
	@MvcFilter(filters = CaptchaFilter.class)
	public String captcha() {
		return "html::Hello World!";
	}
	
//	@GET
//	@Path("api")
//	@MvcFilter(filters = ApiAllowRequestCheck.class)
//	public String aes() {
//		return "html::Hello World!";
//	}

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