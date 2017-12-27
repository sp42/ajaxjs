package com.ajaxjs.simpleApp.controller;


import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.simpleApp.service.NewsService;
import com.ajaxjs.simpleApp.service.NewsServiceImpl;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.Constant;

@Controller
@Path("/home")
public class HomeController implements IController {
	private NewsService service = new NewsServiceImpl();

	@GET
	public String index(ModelAndView model) {
		CommonController.initDb();
		
		try{
			model.put("news", service.getTop5());
		}finally{
			CommonController.closeDb();
		}
		return Constant.jsp_perfix + "home";
	}
}
