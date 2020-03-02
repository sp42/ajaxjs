package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.XMLHelper;

/**
 * 开发工具
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/developer-tool")
public class DeveloperTools implements IController {
	@Path("deve")
	@GET
	public String deve(ModelAndView mv, MvcRequest r) {
		// 数据库管理
		mv.put("list", DataBaseShowStruController.getConnectionConfig(r.mappath("/META-INF/context.xml")));

		// 代码生成器
		Map<String, String> map = XMLHelper.nodeAsMap(r.mappath("/META-INF/context.xml"),
				"//Resource[@name='" + ConfigService.getValueAsString("data.database_node") + "']");
		mv.put("saveFolder", ConfigService.getValueAsString("System.project_folder") + "\\src"); // 臨時保存
		mv.put("conn", map);

		return BaseController.admin("developer-tool");
	}

	@Path("docs")
	@GET
	public String docs() {
		return BaseController.admin("doc");
	}
}
