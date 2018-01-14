package com.ajaxjs.config;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.web.Constant;

@Controller
public abstract class BaseConfigController implements IController {
	@GET
	@Path("siteStru")
	public String siteStruUI() {
		return Constant.common_jsp_perfix + "config/siteStru";
	}

	@GET
	@Path("site")
	public String siteUI() {
		return Constant.common_jsp_perfix + "config/site-config";
	}

	@GET
	public String allUI(ModelAndView model) {
		return Constant.common_jsp_perfix + "config/all-config";
	}

	@POST
	public String save(Map<String, Object> map, HttpServletRequest request) {
		ConfigService.loadJSON_in_JS(map);
		ConfigService.load(); // 刷新配置
		//		
		if (request.getServletContext().getAttribute("all_config") != null)
			request.getServletContext().setAttribute("all_config", ConfigService.config);
		return String.format(Constant.json_ok, "修改配置成功！");
	}
}
