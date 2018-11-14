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
package com.ajaxjs.config;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.Version;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.js.JsonStruTraveler;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 网站结构的配置
 * 
 * @author Sp42 frank@ajaxjs.com
 */
@javax.servlet.annotation.WebListener
public class SiteStruService implements ServletContextListener {
	private static final LogHelper LOGGER = LogHelper.getLog(SiteStruService.class);

	public static SiteStru stru;

	/**
	 * 配置 json 文件的路径
	 */
	public static String jsonPath = Version.srcFolder + "site_stru.json";

	/**
	 * 加载网站结构的配置
	 */
	public static void load() {
		stru = new SiteStru();
		stru.setJsonPath(jsonPath);
		stru.setJsonStr(FileUtil.openAsText(jsonPath));
		stru.addAll(JsonHelper.parseList(stru.getJsonStr()));
		stru.setLoaded(true);
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext cxt = e.getServletContext();
		Version.tomcatVersionDetect(cxt.getServerInfo());

		if (new File(ConfigService.jsonPath).exists()) {
			ConfigService.load();

			if (ConfigService.config.isLoaded()) {
				cxt.setAttribute("aj_allConfig", ConfigService.config); // 所有配置保存在这里

//				String configJson = JsonHelper.format(JsonHelper.stringifyMap(ConfigService.config));
				LOGGER.infoGreen("加载 " + ConfigService.getValueAsString("clientFullName") + " " + cxt.getContextPath()
						+ " 项目配置成功！All config loaded.");
			} else
				LOGGER.warning("加载配置失败！");
		} else
			LOGGER.info("没有项目配置文件");

		if (new File(jsonPath).exists()) {
			load();
			t.travelList(stru);

			cxt.setAttribute("SITE_STRU", this); // 所有网站结构保存在这里
			LOGGER.infoGreen("加载网站的结构文件成功 Site Structure Config Loaded.");
		} else
			LOGGER.info("没有网站的结构文件");

		cxt.setAttribute("ctx", cxt.getContextPath());
		cxt.setAttribute("ajaxjsui", cxt.getContextPath() + "/" + Constant.ajajx_ui);
		cxt.setAttribute("commonAsset", cxt.getContextPath() + "/" + Constant.commonAsset); // 静态资源目录
		cxt.setAttribute("commonJsp", cxt.getContextPath() + "/" + Constant.jsp_perfix);
	}

	/**
	 * 获取导航
	 * 
	 * @return 导航数据
	 */
	public List<Map<String, Object>> getNavBar() {
		return stru;
	}

	/**
	 * JSON 查找器
	 */
	private static final JsonStruTraveler t = new JsonStruTraveler();

	/**
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @param uri         请求地址，例如 "menu/menu-1"
	 * @param contextPath 项目名称
	 * @return 当前页面节点
	 */
	public static Map<String, Object> getPageNode(String uri, String contextPath) {
		// 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
		String path = uri.replace(contextPath, "").replaceAll("/\\d+", "").replaceFirst("/\\w+\\.\\w+$", "");

		if (stru != null && stru.isLoaded()) {
			Map<String, Object> map = t.findByPath(path, stru);
			return map;
		} else
			return null;
	}

	/**
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @param request 请求对象
	 * @return 当前页面节点
	 */
	public static Map<String, Object> getPageNode(HttpServletRequest request) {
		return getPageNode(request.getRequestURI(), request.getContextPath());
	}

	/**
	 * 用于 current 的对比 <li
	 * ${pageContext.request.contextPath.concat('/').concat(menu.fullPath).
	 * concat('/') == pageContext.request.requestURI ? ' class=selected' : ''}> IDE
	 * 语法报错，其实正确的 于是，为了不报错 <li ${PageNode.isCurrentNode(menu) ? ' class=selected' :
	 * ''}>
	 * 
	 * @param node 节点
	 * @return true 表示为是当前节点
	 */
	public boolean isCurrentNode(Map<String, ?> node, HttpServletRequest request) {
		if (node == null || node.get("fullPath") == null)
			return false;

		String uri = request.getRequestURI(), contextPath = request.getContextPath();
		String fullPath = node.get("fullPath").toString(), ui = contextPath.concat("/").concat(fullPath).concat("/");

		return uri.equals(ui) || uri.indexOf(fullPath) != -1;
	}

	/**
	 * 生成二级节点
	 * 
	 * @param request 请求对象
	 * @return 二级节点菜单
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSecondLevelNode(HttpServletRequest request) {
		if (request.getAttribute("secondLevel_Node") == null) {
			String path = getPath(request);

			path = path.substring(1, path.length());
			String second = path.split("/")[0];
			Map<String, Object> map = t.findByPath(second, stru);
			request.setAttribute("secondLevel_Node", map); // 保存二级栏目节点之数据

			return map;
		} else {
			return (Map<String, Object>) request.getAttribute("secondLevel_Node");
		}
	}

	/**
	 * 生成二级节点菜单所需的数据
	 * 
	 * @param request 请求对象
	 * @return 二级节点菜单列表
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, Object>> getMenu(HttpServletRequest request) {
		Map<String, Object> map = getSecondLevelNode(request);
		return map != null && map.get("children") != null ? (List<Map<String, Object>>) map.get("children") : null;
	}

	/**
	 * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
	 * 
	 * @param request 请求对象
	 * @return 资源 URI
	 */
	private static String getPath(HttpServletRequest request) {
		return request.getRequestURI().replace(request.getContextPath(), "").replaceFirst("/\\w+\\.\\w+$", "");
	}

	private static String table = "<table class=\"siteMap\"><tr><td>%s</td></tr></table>",
			a = "<a href=\"%s/\" class=\"indentBlock_%s\"><span class=\"dot\">·</span>%s</a>\n ",
			newCol = "\n\t</td>\n\t<td>\n\t\t";

	private String siteMapCache;

	/**
	 * 获取页脚的网站地图
	 * 
	 * @return 页脚的网站地图
	 */
	public String getSiteMap(HttpServletRequest request) {
		if (siteMapCache == null) 
			siteMapCache = getSiteMap(stru, request.getContextPath());

		return siteMapCache;
	}

	/**
	 * 获取页脚的网站地图
	 * 
	 * @param list        可指定数据
	 * @param contextPath
	 * @return 页脚的网站地图
	 */
	public static String getSiteMap(List<Map<String, Object>> list, String contextPath) {
		StringBuilder sb = new StringBuilder();
		getSiteMap(list, sb, contextPath);

		return String.format(table, sb.toString());
	}

	/**
	 * 该函数递归使用，故须独立成一个函数
	 * 
	 * @param list
	 * @param sb
	 * @param contextPath
	 */
	@SuppressWarnings("unchecked")
	private static void getSiteMap(List<Map<String, Object>> list, StringBuilder sb, String contextPath) {
		for (Map<String, Object> map : list) {
			if (map != null) {
				if (0 == (int) map.get("level")) // 新的一列
					sb.append(newCol);

				sb.append(String.format(a, contextPath + map.get("fullPath").toString(), map.get("level").toString(),
						map.get("name").toString()));

				if (map.get("children") != null && map.get("children") instanceof List)
					getSiteMap((List<Map<String, Object>>) map.get("children"), sb, contextPath);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
