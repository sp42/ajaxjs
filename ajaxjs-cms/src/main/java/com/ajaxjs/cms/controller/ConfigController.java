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
package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.Constant;
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
	/**
	 * 全部配置
	 * 
	 * @param model
	 * @return
	 */
	@GET
	public String allConfig(ModelAndView model) {
		model.put("configJson", FileUtil.openAsText(ConfigService.jsonPath));
		model.put("jsonSchemePath", FileUtil.openAsText(ConfigService.jsonSchemePath));

		return BaseController.cms("config-all");
	}

	@GET
	@Path("siteStru")
	public String siteStruUI() {
		return BaseController.cms("config-site-stru");
	}

	@GET
	@Path("site")
	public String siteUI() {
		return BaseController.cms("config-site-form");
	}

	/**
	 * 保存配置并且刷新配置
	 * 
	 * @param map 配置的 JSON 字符串
	 * @param request
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String saveAllconfig(Map<String, Object> map, HttpServletRequest request) {
		ConfigService.loadJSON_in_JS(map);
		ConfigService.load(); // 刷新配置

		if (request.getServletContext().getAttribute("aj_allConfig") != null)
			request.getServletContext().setAttribute("aj_allConfig", ConfigService.config);

		return Constant.jsonOk("修改配置成功！");
	}

	/**
	 * 保存网站结构
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@POST
	@Path("site")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveSite(Map<String, Object> map, HttpServletRequest request) {
		return saveAllconfig(map, request);
	}
}
