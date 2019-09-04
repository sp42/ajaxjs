package com.mysoft.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;

@Path("/SayHello") // 定义URL路径
public class HelloWorldController implements IController {
	@GET	// 绑定 HTTP GET 方法，用户一访问 GET /SayHello，就会跑到这方法来
	public String helloWorld() {
		return "html::Hello World!"; // 返回字符串就是要显示的内容，html:: 是固有的前缀
	}
}
