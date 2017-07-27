/**
 * Copyright Frank Cheung frank@ajaxjs.com
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

import com.ajaxjs.Version;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.js.JsonStruTraveler;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 网站结构的配置
 * @author Frank Cheung frank@ajaxjs.com
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

		if (ConfigService.config.isLoaded())
			cxt.setAttribute("all_config", ConfigService.config); // 所有配置保存在这里

		if (new File(jsonPath).exists()) {
			load();
			LOGGER.info("加载网站的结构文件成功");
		} else
			LOGGER.info("没有网站的结构文件");
	}
	
	public static void main(String[] args) {
		load();
		t.travleList(stru, "", 0);
		
		System.out.println(stru);
		System.out.println(getSiteMap(stru));
	}

	/**
	 * 获取导航
	 * 
	 * @return 导航数据
	 */
	public static List<Map<String, Object>> getNavBar() {
		return stru;
	}

	/**
	 * JSON 查找器
	 */
	private static final JsonStruTraveler t = new JsonStruTraveler();

	/**
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @param path
	 *            例如 "menu/menu-1"
	 * @return 当前页面节点
	 */
	public static Map<String, Object> getPageNode(String uri, String contextPath) {
		// 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
		String path = uri.replace(contextPath, "").replaceFirst("/\\w+\\.\\w+$", "");
		Map<String, Object> map = t.find(path, stru);
		
		return map;
	}

	private final static String 
		table = "<table class=\"siteMap\"><tr><td>%s</td></tr></table>", 
		a = "<a href=\"%s\" class=\"indentBlock_%s\"><span class=\"dot\">·</span>%s</a>\n ",
		newCol = "\n\t</td>\n\t<td>\n\t\t";
	
	/**
	 * 获取页脚的网站地图
	 * 
	 * @return 页脚的网站地图
	 */
	public static String getSiteMap(List<Map<String, Object>> list) {
		StringBuilder sb = new StringBuilder();
		getSiteMap(list, sb);
		
		return String.format(table, sb.toString());
	}
	
	/**
	 * 该函数递归使用，故须独立成一个函数
	 * @param list
	 * @param sb
	 */
	@SuppressWarnings("unchecked")
	private static void getSiteMap(List<Map<String, Object>> list, StringBuilder sb) {
		for (Map<String, Object> map :list) {
			if (map != null) {
				sb.append(String.format(a, map.get("fullPath").toString(), map.get("level").toString(), map.get("name").toString()));
				
				if(0 == (int)map.get("level")) // 新的一列
					sb.append(newCol);
				
				if (map.get("children") != null && map.get("children") instanceof List) 
					getSiteMap((List<Map<String, Object>>) map.get("children"),  sb);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
