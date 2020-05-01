/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
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
 * @author sp42 frank@ajaxjs.com
 */
@javax.servlet.annotation.WebListener
public class SiteStruService implements ServletContextListener {
	private static final LogHelper LOGGER = LogHelper.getLog(SiteStruService.class);

	/**
	 * 保存网站结构的 Map
	 */
	public static SiteStru stru;

	/**
	 * 数据库的数据转换为菜单显示。需要 Servlet 启动时从数据库拉取数据并静态保存
	 * 
	 * @param list
	 * @param cxt  Servlet 上下文
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
		ConfigService.SCHEME_JSON = ConfigScheme.JSON;

		String configJson = ConfigService.CONFIG_JSON_PATH;

		if (configJson == null) {
			configJson = ctx.getRealPath("/META-INF/site_config.json");
			ConfigService.CONFIG_JSON_PATH = configJson;
		}

		if (!new File(configJson).exists())
			LOGGER.info("没有项目配置文件");
		else {
			ConfigService.load(configJson);

			if (ConfigService.CONFIG.isLoaded()) {
				ctx.setAttribute("aj_allConfig", ConfigService.CONFIG); // 所有配置保存在这里

				// String configJson =
				// JsonHelper.format(JsonHelper.stringifyMap(ConfigService.config));
				LOGGER.infoGreen("加载 " + ConfigService.getValueAsString("clientFullName") + " " + ctx.getContextPath()
						+ " 项目配置成功！All config loaded.");

				if (ConfigService.getValueAsBool("isForceProductEnv")) {
					LOGGER.infoGreen("强制为生产环境模式 isDebug=false");
					Version.isDebug = false;
				}

				onStartUp(ctx);
			} else
				LOGGER.warning("加载配置失败！");
		}

		loadSiteStru(ctx);
		if (stru.isLoaded())
			ctx.setAttribute("SITE_STRU", this); // 所有网站结构保存在这里

		String ctxPath = ctx.getContextPath();
		String ajaxjsui = Version.isDebug ? "http://" + Tools.getIp() + ":8080/ajaxjs-js"
				: ctxPath + "/" + Constant.ajajx_ui;

		ctx.setAttribute("ctx", ctxPath);
		ctx.setAttribute("ajaxjsui", ajaxjsui);
		ctx.setAttribute("commonAsset", ctxPath + "/asset/common"); // 静态资源目录
		ctx.setAttribute("commonAssetIcon", ctxPath + "/asset/common/icon"); // 静态资源目录
		ctx.setAttribute("commonJsp", ctxPath + "/" + Constant.jsp_perfix);
		ctx.setAttribute("isDebuging", Version.isDebug);
		ctx.setAttribute("ajaxjs_ui_output", "http://static.ajaxjs.com/");
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
				if (clz != null) {
					ServletStartUp startUp = ReflectUtil.newInstance(clz);
					startUp.onStartUp(cxt);
				}
			} catch (Throwable e) {
				if (e instanceof UndeclaredThrowableException) {
					Throwable _e = ReflectUtil.getUnderLayerErr(e);
					LOGGER.warning(_e);
				}

				throw e;
			}
		}
	}

	/**
	 * 加载网站结构
	 * 
	 * @param cxt Servlet 上下文
	 */
	public static void loadSiteStru(ServletContext cxt) {
		load(cxt);
		ListMap.buildPath(stru, true);
//		t.travelList(stru);
		LOGGER.infoGreen("加载网站的结构文件成功 Site Structure Config Loaded.");
	}

	/**
	 * 配置 json 文件的路径
	 */
	public static String JSON_PATH = Version.srcFolder + File.separator + "site_stru.json";

	/**
	 * 加载网站结构的配置
	 * 
	 * @param ctx Servlet 上下文
	 */
	public static void load(ServletContext ctx) {
		String json = JSON_PATH;

		if (new File(JSON_PATH).exists())
			json = JSON_PATH;
		else
			json = ctx.getRealPath("/META-INF/site_stru.json");

		if (new File(json).exists()) {
			stru = new SiteStru();
			stru.setJsonPath(json);
			stru.setJsonStr(FileHelper.openAsText(json));
			stru.clear();
			stru.addAll(db2menu(JsonHelper.parseList(stru.getJsonStr()), ctx));
			stru.setLoaded(true);
		} else
			LOGGER.info("没有网站的结构文件");
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
	 * @param uri         请求地址，例如 "menu/menu-1"
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
		return map != null && map.get(ListMap.CHILDREN) != null ? (List<Map<String, Object>>) map.get(ListMap.CHILDREN)
				: null;
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

	private static String TABLE = "<table class=\"siteMap\"><tr><td>%s</td></tr></table>",
			A_LINK = "<a href=\"%s/\" class=\"indentBlock_%s\"><span class=\"dot\">·</span>%s</a>\n ",
			NEW_COL = "\n\t</td>\n\t<td>\n\t\t";

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

		return String.format(TABLE, sb.toString());
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
				Object isHidden = map.get("isHidden");
				if (isHidden != null && ((boolean) isHidden) == true) // 隐藏的
					continue;

				if (0 == (int) map.get(ListMap.LEVEL)) // 新的一列
					sb.append(NEW_COL);

				sb.append(String.format(A_LINK, contextPath + map.get(ListMap.PATH).toString(),
						map.get(ListMap.LEVEL).toString(), map.get("name").toString()));

				if (map.get(ListMap.CHILDREN) != null && map.get(ListMap.CHILDREN) instanceof List)
					getSiteMap((List<Map<String, Object>>) map.get(ListMap.CHILDREN), sb, contextPath);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
