/**
 * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ajaxjs.app;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.config.SiteStruService;
import com.ajaxjs.util.XmlHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.IController;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 编辑配置的控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/admin/config")
public class ConfigController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(ConfigController.class);

	@GET
	public String config(ModelAndView mv, MvcRequest r) {
		LOGGER.info("参数配置");

		mv.put("conn", XmlHelper.nodeAsMap(r.mappath("/META-INF/context.xml"), "//Resource[@name='" + ConfigService.get("data.database_node") + "']"));
		loadJson(mv);

		return BaseController.jsp("app/config/config");
	}

	// TODO
	private static void loadJson(ModelAndView model) {
		model.put("configJson", FileHelper.openAsText(ConfigService.jsonPath));
		model.put("schemeJson", ConfigService.getSchemeJson());
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String saveAllconfig(Map<String, Object> map, HttpServletRequest request) {
		LOGGER.info("保存配置并且刷新配置");

		ConfigService.loadJSON_in_JS(map);
		ConfigService.load(ConfigService.jsonPath); // 刷新配置

		if (request.getServletContext().getAttribute("aj_allConfig") != null)
			request.getServletContext().setAttribute("aj_allConfig", ConfigService.CONFIG);

		return BaseController.jsonOk("修改配置成功！");
	}

	@GET
	@Path("site")
	public String siteUI() {
		LOGGER.info("编辑网站信息");
		return BaseController.admin("config/config-site-form");
	}

	@POST
	@Path("site")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveSite(Map<String, Object> map, HttpServletRequest request) {
		LOGGER.info("保存网站信息");
		return saveAllconfig(map, request);
	}

	@GET
	@Path("siteStru")
	public String siteStruUI(ModelAndView model) {
		LOGGER.info("编辑网站结构");

		model.put("siteStruJson", FileHelper.openAsText(SiteStruService.STRU.getJsonPath()));

		return BaseController.admin("config/config-site-stru");
	}

	@POST
	@Path("siteStru")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveSiteStru(@NotNull @FormParam("json") String json) {
		LOGGER.info("保存网站结构");

		FileHelper.saveText(SiteStruService.STRU.getJsonPath(), json);
		SiteStruService.loadSiteStru(MvcRequest.getHttpServletRequest().getServletContext());

		return BaseController.jsonOk("修改网站结构成功！");
	}

	@POST
	@Path("siteStru/initJSP")
	@Produces(MediaType.APPLICATION_JSON)
	public String siteStruUI_initJSP(@FormParam("path") String path, MvcRequest r) {
		LOGGER.info("初始化 JSP 页面");

		String folder = r.mappath(path);
		FileHelper.mkDir(folder);
		String dest = folder + File.separator + "index.jsp";
		FileHelper.copy(r.mappath(BaseController.JSP_PERFIX_WEBINF + File.separator + "common-page.jsp"), dest, true);

		return BaseController.jsonOk("初始化 JSP 页面成功！");
	}
}
