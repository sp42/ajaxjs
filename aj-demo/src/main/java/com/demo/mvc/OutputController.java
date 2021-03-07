package com.demo.mvc;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.web.mvc.IController;
import com.ajaxjs.web.mvc.ModelAndView;
import com.demo.orm.dao.News;

@Path("/output")
public class OutputController implements IController {
	@GET
	public String jsp(ModelAndView mv) {
		mv.put("showText", "测试字符串"); // 测试字符串
		mv.put("showLong", 100000L); // 测试 long
		mv.put("list", new ArrayList<Object>() { // 测试列表
			private static final long serialVersionUID = 1L;
			{
				add("foo");
				add(123456);
				add(true);
			}
		});
		mv.put("array", new String[] { "abc", "bar", "12345a" });// 测试数组
		mv.put("map", new HashMap<String, Object>() {// //测试 Map
			private static final long serialVersionUID = 1L;
			{
				put("key1", "FOO");
				put("key2", "BAR");
				put("key3", false);
			}
		});

		return "/WEB-INF/jsp/output"; // 可以省略 .jsp 的扩展名
	}

	@GET
	@Path("jsonStr")
	public String toJson() {
		return BaseController.toJson("{\"foo\":\"bar\"}", true);
	}
	
	@GET
	@Path("jsonMap")
	public String toJsonMap() {
		return BaseController.toJson(new HashMap<String, Object>() {// //测试 Map
			private static final long serialVersionUID = 1L;
			{
				put("key1", "FOO");
				put("key2", "BAR");
				put("key3", false);
			}
		});
	}
	
	@GET
	@Path("jsonList")
	public String toJsonList() {
		return BaseController.toJson(new ArrayList<Object>() { // 测试列表
			private static final long serialVersionUID = 1L;
			{
				add("foo");
				add(123456);
				add(true);
			}
		});
	}
	
	@GET
	@Path("jsonBean")
	public String toJsonBean() {
		News news = new News();
		news.setId(1000L);
		news.setName("foo");
		
		return BaseController.toJson(news);
	}
}
