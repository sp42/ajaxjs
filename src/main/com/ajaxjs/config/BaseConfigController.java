package com.ajaxjs.config;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.simpleApp.Constant;
import com.ajaxjs.util.io.FileUtil;

@Controller
public abstract class BaseConfigController implements IController {
	@GET
	public String allUI(ModelAndView model) {
		model.put("configJson", FileUtil.openAsText(ConfigService.jsonPath));
		model.put("jsonSchemePath", FileUtil.openAsText(ConfigService.jsonSchemePath));
		
		return Constant.common_jsp_perfix + "config/all-config";
	}
	
	@POST
	public String saveAllconfig(Map<String, Object> map, HttpServletRequest request) {
		ConfigService.loadJSON_in_JS(map);
		ConfigService.load(); // 刷新配置

		if (request.getServletContext().getAttribute("all_config") != null)
			request.getServletContext().setAttribute("all_config", ConfigService.config);
		
		return String.format(Constant.json_ok, "修改配置成功！");
	}
	
	@GET
	@Path("siteStru")
	public String siteStruUI() {
		return Constant.common_jsp_perfix + "config/site-stru";
	}

	@GET
	@Path("site")
	public String siteUI() {
		return Constant.common_jsp_perfix + "config/site-config";
	}

	@POST
	@Path("site")
	public String saveSite(Map<String, Object> map, HttpServletRequest request) {
		return saveAllconfig(map, request);
	}
}
