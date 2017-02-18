/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.web.config;

import java.util.List;
import java.util.Map;

/**
 * JSONPath
 * 
 * @author frank
 *
 */
public class NodeProcessor {
	/**
	 * 项目地址
	 */
	private String contextPath;

	/**
	 * 请求地址
	 */
	private String uri;

	/**
	 * 页头导航的缓存
	 */
	private static List<Map<String, Object>> navList;

	/**
	 * 页面节点信息
	 */
	private Map<String, Object> pageNode;

	/**
	 * 页脚导航的缓存
	 */
	private static String footerList;

	/**
	 * 创建一个节点对象
	 * 
	 * @param contextPath
	 *            项目名称
	 * @param uri
	 *            请求路径
	 * @param jsruntime
	 *            JS 引擎
	 */
	public NodeProcessor(String contextPath, String uri) {
		this.contextPath = contextPath;
		this.uri = uri;
	}

	/**
	 * 获取导航
	 * 
	 * @return 导航数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNavBar() {
		if (navList == null) {
			navList = InitConfig.allConfig.eval("bf.AppStru.getNav();", List.class);
		}

		return navList;
	}

	/**
	 * 获取当前页面节点，并带有丰富的节点信息
	 * 
	 * @return 当前页面节点
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPageNode() {
		if (pageNode == null) {
			String jsCode = String.format("bf.AppStru.getPageNode('%s', '%s');", getRoute(), contextPath);
			pageNode = (Map<String, Object>) InitConfig.allConfig.eval(jsCode, Map.class);
		}

		return pageNode;
	}

	/**
	 * 获取页脚的网站地图，有缓冲考虑
	 * 
	 * @return 页脚的网站地图
	 */
	public String getSiteMap() {
		if (footerList == null) {
			String code = String.format("JSON_Tree.util.makeSiteMap(bf.AppStru.data, '%s');", contextPath);
			footerList = InitConfig.allConfig.eval(code, String.class);
		}

		return footerList;
	}

	/**
	 * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源（最原始的版本）
	 * 
	 * @return
	 */
	public String getRoute() {
		return uri.replace(contextPath, "").replaceFirst("/\\w+\\.\\w+$", ""); // 删除 index.jsp
	}

	/**
	 * 用于 current 的对比 <li
	 * ${pageContext.request.contextPath.concat('/').concat(menu.fullPath).
	 * concat('/') == pageContext.request.requestURI ? ' class=selected' : ''}>
	 * IDE 语法报错，其实正确的 于是，为了不报错 <li ${PageNode.isCurrentNode(menu) ? '
	 * class=selected' : ''}>
	 * 
	 * @param node
	 *            节点
	 * @return true 表示为是当前节点
	 */
	public boolean isCurrentNode(Map<String, ?> node) {
		if (node == null || node.get("fullPath") == null)
			return false;

		String fullPath = node.get("fullPath").toString(), ui = contextPath.concat("/").concat(fullPath).concat("/");

		return uri.equals(ui) || uri.indexOf(fullPath) != -1;
	}

	/**
	 * 读取网站结构中的某个节点。如果网站结构中没有这个节点对象信息返回 null。
	 * 
	 * @param nodePath
	 *            指定的节点路径，以 / 分隔，例如 /product/app
	 * @return 如果网站结构中没有这个节点对象信息返回 null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNode(String nodePath) {
		return InitConfig.allConfig.eval(String.format("bf.AppStru.getNode('%s');", nodePath), Map.class);
	}

	/**
	 * 读取当前节点信息，返回 hash 结构（详细信息） ${pageNode.node.name}
	 * 
	 * @deprecated 与 getPageNode 重复
	 * @return 当前节点信息
	 */
	public Map<String, Object> getNode() {
		return getNode(getRoute());
	}
	
	 /**
	 * 返回第几级
	 *
	 * @return
	 */
	// public int getLevels() {
	// String route = getRoute();
	// String[] arr = route.split("/");
	// return arr.length;
	// }
}