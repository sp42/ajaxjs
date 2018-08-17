/**
 * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.simpleApp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.io.FileUtil;

/**
 * 编辑配置的控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/config")
public class ConfigController implements IController {
	@GET
	public String allUI(ModelAndView model) {
		model.put("configJson", FileUtil.openAsText(ConfigService.jsonPath));
		model.put("jsonSchemePath", FileUtil.openAsText(ConfigService.jsonSchemePath));

		return Constant.commonJsp + "config/all-config";
	}

	@POST
	@Produces("json")
	public String saveAllconfig(Map<String, Object> map, HttpServletRequest request) {
		ConfigService.loadJSON_in_JS(map);
		ConfigService.load(); // 刷新配置

		if (request.getServletContext().getAttribute("all_config") != null)
			request.getServletContext().setAttribute("all_config", ConfigService.config);

		return CommonController.jsonOk("修改配置成功！");
	}

	@GET
	@Path("siteStru")
	public String siteStruUI() {
		return Constant.commonJsp + "config/site-stru";
	}

	@GET
	@Path("site")
	public String siteUI() {
		return Constant.commonJsp + "config/site-config";
	}

	@POST
	@Path("site")
	@Produces("json")
	public String saveSite(Map<String, Object> map, HttpServletRequest request) {
		return saveAllconfig(map, request);
	}
}
