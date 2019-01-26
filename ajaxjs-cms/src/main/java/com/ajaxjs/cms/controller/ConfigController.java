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
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.io.FileUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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

		return BaseController.cms("config-all");
	}

	@Operation(
		summary = "保存配置", 
		tags = { "SystemConfig", "Admin Service ONLY" }, 
		description = "保存配置并且刷新配置", 
		responses = {
			@ApiResponse(description = "操作是否成功", content = @Content(mediaType = "application/json")) 
		}
	)
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String saveAllconfig(@Parameter(description = "配置的 JSON 字符串", required = true) Map<String, Object> map, @Parameter(hidden = true) HttpServletRequest request) {
		ConfigService.loadJSON_in_JS(map);
		ConfigService.load(); // 刷新配置

		if (request.getServletContext().getAttribute("aj_allConfig") != null)
			request.getServletContext().setAttribute("aj_allConfig", ConfigService.config);

		return JsonHelper.jsonOk("修改配置成功！");
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

	@Operation(summary = "保存网站结构", tags = { "SystemConfig", "Admin Service ONLY" }, description = "保存网站结构", responses = { @ApiResponse(description = "操作是否成功", content = @Content(mediaType = "application/json")) })
	@POST
	@Path("site")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveSite(Map<String, Object> map, @Parameter(hidden = true) HttpServletRequest request) {
		return saveAllconfig(map, request);
	}
}
