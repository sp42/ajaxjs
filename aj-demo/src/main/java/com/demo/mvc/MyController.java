package com.demo.mvc;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.web.mvc.IController;

@Path("/foo") // 定义总的URL路径
public class MyController implements IController {
	@GET
	public String showHTML() {
		return "html::Hello World!";
	}

	@GET
	@Path("jsp") // 与类的@Path注解里的路径组合
	public String gotoJSP() {
		return "/jsp/my-jsp.jsp";
	}

	@POST
	@Path("/bar/blog") // 注意斜杠/开头，表示不与类的@Path注解里的路径组合
	public String redirect() {
		return "redirect::http://blog.csdn.net/zhangxin09";
	}

	@PUT
	public String showJSON() {
		return "json::{\"name\":\"Jack\"}";
	}

	@DELETE
	@Path("/admin/{root}/{id}")
	public String editUI() {
		return "app/article-edit";
	}
}