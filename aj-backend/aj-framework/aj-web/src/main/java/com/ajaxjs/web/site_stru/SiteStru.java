package com.ajaxjs.web.site_stru;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.ListMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网站结构的配置
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SiteStru extends ArrayList<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(SiteStru.class);

	private static final long serialVersionUID = 9099886055914666661L;

	/**
	 * 配置文件路径
	 */
	private String filePath;

	/**
	 * 创建一个配置器
	 * 
	 * @param filePath 配置文件路径
	 */
	public SiteStru(String filePath) {
		super();
		this.filePath = filePath;
		load();
	}

	/**
	 * 创建一个配置器
	 */
	public SiteStru() {
		super();
		filePath = Resources.getResourcesFromClasspath("site_stru.json");
		load();
	}

	/**
	 * 加载 JSON 配置
	 */
	public void load() {
		if (!new File(filePath).exists()) {
			LOGGER.info("没有[{0}]网站结构文件", filePath);
			return;
		}

		loaded = false;

		try {
			String jsonStr = FileHelper.openAsText(filePath);
			List<Map<String, Object>> map = JsonHelper.parseList(jsonStr);

			ListMap.buildPath(map, true);

			clear();
			addAll(map);
			loaded = true;

			LOGGER.infoGreen("加载网站结构");
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.warning("加载网站结构失败", e); // 可能 JSON 解析异常，搞到 Spring 都启动不了，加个 try/catch
		}
	}

	/**
	 * 保存配置
	 * 
	 * @param jsonStr
	 */
	public void save(String jsonStr) {
		FileHelper.saveText(filePath, jsonStr);
		load();
	}

	/**
	 * 是否加载成功
	 */
	private boolean loaded;

	/**
	 * 获取导航
	 * 
	 * @return 导航数据
	 */
	public List<Map<String, Object>> getNavBar() {
		return this;
	}

	/**
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @param uri         请求地址，例如 "menu/menu-1"
	 * @param contextPath 项目名称
	 * @return 当前页面节点
	 */
	public Map<String, Object> getPageNode(String uri, String contextPath) {
		// 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
		String path = uri.replace(contextPath, "").replaceAll("/\\d+", "").replaceFirst("/\\w+\\.\\w+$", "");

		if (loaded) {
			Map<String, Object> map = ListMap.findByPath(path, this);
			return map;
		} else
			return null;
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

	static String CTX_KEY = "SITE_STRU";

	/**
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @param request 请求对象
	 * @return 当前页面节点
	 */
	public static Map<String, Object> getPageNode(HttpServletRequest request) {
		SiteStru stru = (SiteStru) request.getServletContext().getAttribute(CTX_KEY);
		return stru.getPageNode(request.getRequestURI(), request.getContextPath());
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

			if (!StringUtils.hasText(path))
				return null;

			path = path.substring(1, path.length());
			String second = path.split("/")[0];
			Map<String, Object> map = ListMap.findByPath(second, this);
			request.setAttribute("secondLevel_Node", map); // 保存二级栏目节点之数据

			return map;
		} else
			return (Map<String, Object>) request.getAttribute("secondLevel_Node");
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

	private static String TABLE = "<table class=\"siteMap\"><tr><td>%s</td></tr></table>",
			A_LINK = "<a href=\"%s/\" class=\"indentBlock_%s\"><span class=\"dot\">·</span>%s</a>\n ", NEW_COL = "\n\t</td>\n\t<td>\n\t\t";

	private String siteMapCache;

	/**
	 * 获取页脚的网站地图
	 * 
	 * @return 页脚的网站地图
	 */
	public String getSiteMap(HttpServletRequest request) {
		if (siteMapCache == null)
			siteMapCache = getSiteMap(this, request.getContextPath());

		return siteMapCache;
	}

	/**
	 * 获取页脚的网站地图
	 * 
	 * @param list    可指定数据
	 * @param cxtPath
	 * @return 页脚的网站地图
	 */
	public static String getSiteMap(List<Map<String, Object>> list, String cxtPath) {
		StringBuilder sb = new StringBuilder();
		getSiteMap(list, sb, cxtPath);

		return String.format(TABLE, sb.toString());
	}

	/**
	 * 该函数递归使用，故须独立成一个函数
	 * 
	 * @param list
	 * @param sb
	 * @param cxtPath
	 */
	@SuppressWarnings("unchecked")
	private static void getSiteMap(List<Map<String, Object>> list, StringBuilder sb, String cxtPath) {
		for (Map<String, Object> map : list) {
			if (map != null) {
				Object isHidden = map.get("isHidden");
				if (isHidden != null && ((boolean) isHidden) == true) // 隐藏的
					continue;

				if (0 == (int) map.get(ListMap.LEVEL)) // 新的一列
					sb.append(NEW_COL);

				sb.append(String.format(A_LINK, cxtPath + map.get(ListMap.PATH).toString(), map.get(ListMap.LEVEL).toString(),
						map.get("name").toString()));

				if (map.get(ListMap.CHILDREN) != null && map.get(ListMap.CHILDREN) instanceof List)
					getSiteMap((List<Map<String, Object>>) map.get(ListMap.CHILDREN), sb, cxtPath);
			}
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}