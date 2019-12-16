/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.config;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.Version;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.ListMap;
import com.ajaxjs.util.map.ListMapConfig;

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
	public static String jsonPath = Version.srcFolder + File.separator + "site_stru.json";

	/**
	 * 加载网站结构的配置
	 */
	public static void load(ServletContext ctx) {
		stru = new SiteStru();
		stru.setJsonPath(jsonPath);
		stru.setJsonStr(FileHelper.openAsText(jsonPath));
		stru.clear();
		stru.addAll(db2menu(JsonHelper.parseList(stru.getJsonStr()), ctx));
		stru.setLoaded(true);
	}

	/**
	 * 数据库的数据转换为菜单显示。需要 Servlet 启动时从数据库拉取数据并静态保存
	 * 
	 * @param list
	 * @param cxt
	 * @return
	 */
	private static List<Map<String, Object>> db2menu(List<Map<String, Object>> list, ServletContext cxt) {
		ListMapConfig c = new ListMapConfig();

		c.mapHandler = new ListMapConfig.MapHandler() {
			@Override
			public boolean execute(Map<String, Object> map, Map<String, Object> superMap, int level) {
				if (map.containsKey("dbNode")) {
					Object _map = cxt.getAttribute(map.get("dbNode").toString());
					Objects.requireNonNull(_map, "Servlet 初始化数据未准备好，依赖数据：" + map.get("dbNode").toString());

					@SuppressWarnings("unchecked")
					Map<Long, BaseModel> data = (Map<Long, BaseModel>) _map;

					List<Map<String, Object>> list = new ArrayList<>();
					for (Long id : data.keySet()) {
						Map<String, Object> cMap = new HashMap<>();
						cMap.put("id", "?catelogId=" + id);
						cMap.put("name", data.get(id).getName());
						list.add(cMap);
					}
					map.put("children", list);
					// 转换为符合 menu 的格式
				}

				return false;
			}
		};

		ListMap.traveler(list, c);

		return list;
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext ctx = e.getServletContext();
		Version.tomcatVersionDetect(ctx.getServerInfo());
		
		System.out.println(ConfigService.jsonPath);

		if (new File(ConfigService.jsonPath).exists()) {
			ConfigService.load();

			if (ConfigService.config.isLoaded()) {
				ctx.setAttribute("aj_allConfig", ConfigService.config); // 所有配置保存在这里

				// String configJson = JsonHelper.format(JsonHelper.stringifyMap(ConfigService.config));
				LOGGER.infoGreen("加载 " + ConfigService.getValueAsString("clientFullName") + " " + ctx.getContextPath() + " 项目配置成功！All config loaded.");

				onStartUp(ctx);
			} else
				LOGGER.warning("加载配置失败！");
		} else
			LOGGER.info("没有项目配置文件");

		if (new File(jsonPath).exists()) {
			loadSiteStru(ctx);
			ctx.setAttribute("SITE_STRU", this); // 所有网站结构保存在这里
		} else
			LOGGER.info("没有网站的结构文件");

		String ctxPath = ctx.getContextPath();
		String ajaxjsui = Version.isDebug ? "http://" + Tools.getIp() + ":8080/ajaxjs-web-js" : ctxPath + "/" + Constant.ajajx_ui;

		ctx.setAttribute("ctx", ctxPath);
		ctx.setAttribute("ajaxjsui", ajaxjsui);
		ctx.setAttribute("commonAsset", ctxPath + "/asset/common"); // 静态资源目录
		ctx.setAttribute("commonAssetIcon", ctxPath + "/asset/common/icon"); // 静态资源目录
		ctx.setAttribute("commonJsp", ctxPath + "/" + Constant.jsp_perfix);
		ctx.setAttribute("isDebuging", Version.isDebug);
		ctx.setAttribute("ajaxjs_ui_output", ctxPath + "/ajaxjs-ui-output");
	}

	/**
	 * Startup callback，外界可调用改方法进行刷新
	 */
	private static void onStartUp(ServletContext cxt) {
		String startUp_Class = ConfigService.getValueAsString("startUp_Class");

		if (!CommonUtil.isEmptyString(startUp_Class)) {
			LOGGER.info("执行 Servlet 启动回调");
			try {				
				Class<ServletStartUp> clz = ReflectUtil.getClassByName(startUp_Class, ServletStartUp.class);
				ServletStartUp startUp = ReflectUtil.newInstance(clz);
				startUp.onStartUp(cxt);
			}catch(Throwable e) {
				if(e instanceof UndeclaredThrowableException) {
					Throwable _e = ReflectUtil.getUnderLayerErr(e);
					_e.printStackTrace();
					
				}
				
				throw e;
			}
		}
	}

	public static void loadSiteStru(ServletContext cxt) {
		load(cxt);
		ListMap.buildPath(stru, true);
//		t.travelList(stru);
		LOGGER.infoGreen("加载网站的结构文件成功 Site Structure Config Loaded.");
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
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @param uri 请求地址，例如 "menu/menu-1"
	 * @param contextPath 项目名称
	 * @return 当前页面节点
	 */
	public static Map<String, Object> getPageNode(String uri, String contextPath) {
		// 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
		String path = uri.replace(contextPath, "").replaceAll("/\\d+", "").replaceFirst("/\\w+\\.\\w+$", "");

		if (stru != null && stru.isLoaded()) {
			Map<String, Object> map = ListMap.findByPath(path, stru);
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
	 * 用于 current 的对比 <li ${pageContext.request.contextPath.concat('/').concat(menu.fullPath). concat('/') == pageContext.request.requestURI ? '
	 * class=selected' : ''}> IDE 语法报错，其实正确的 于是，为了不报错 <li ${PageNode.isCurrentNode(menu) ? ' class=selected' : ''}>
	 * 
	 * @param node 节点
	 * @return true 表示为是当前节点
	 */
	public boolean isCurrentNode(Map<String, ?> node, HttpServletRequest request) {
		if (node == null || node.get(ListMap.PATH) == null)
			return false;

		String uri = request.getRequestURI(), contextPath = request.getContextPath();
		String fullPath = node.get(ListMap.PATH).toString(), ui = contextPath.concat("/").concat(fullPath).concat("/");

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
			Map<String, Object> map = ListMap.findByPath(second, stru);
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
		return map != null && map.get(ListMap.CHILDREN) != null ? (List<Map<String, Object>>) map.get(ListMap.CHILDREN) : null;
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

	private static String table = "<table class=\"siteMap\"><tr><td>%s</td></tr></table>", a = "<a href=\"%s\" class=\"indentBlock_%s\"><span class=\"dot\">·</span>%s</a>\n ", newCol = "\n\t</td>\n\t<td>\n\t\t";

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
	 * @param list 可指定数据
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
				if (0 == (int) map.get(ListMap.LEVEL)) // 新的一列
					sb.append(newCol);
				sb.append(String.format(a, contextPath + map.get(ListMap.PATH).toString(), map.get(ListMap.LEVEL).toString(), map.get("name").toString()));

				if (map.get(ListMap.CHILDREN) != null && map.get(ListMap.CHILDREN) instanceof List)
					getSiteMap((List<Map<String, Object>>) map.get(ListMap.CHILDREN), sb, contextPath);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
