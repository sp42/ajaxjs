package com.ajaxjs.simpleApp.controller;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.simpleApp.service.NewsService;
import com.ajaxjs.simpleApp.service.NewsServiceImpl;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryReadOnlyController;

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
		return "";
		//model.put("catalogMenu", service.getTop5());
		//return String.format(jsp_list, service.getTableName());
	}
}
