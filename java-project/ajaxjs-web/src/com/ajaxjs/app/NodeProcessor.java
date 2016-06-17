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
package com.ajaxjs.app;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.Constant;
import com.ajaxjs.json.ToJavaType;
import com.ajaxjs.util.Util;
import com.ajaxjs.web.UA;

/**
 * JSONPath
 * 
 * @author frank
 *
 */
public class NodeProcessor extends HttpServletRequestWrapper {
	private Map<String, Object> pageNode;
	
	private ToJavaType js; 
	
	/**
	 * 创建一个节点对象
	 * @param request
	 */
	public NodeProcessor(HttpServletRequest request) {
		super(request);
		setAttribute("PageNode", this);// 
		js = (ToJavaType)ConfigListener.jsRuntime;

		String route = getRoute();
		
//		if (ConfigListener.isSite_stru_Loaded && !route.equals("/")) {// 获取当前页面节点，并带有丰富的节点信息
//			String jsCode = String.format("bf.AppStru.getPageNode('%s', '%s');", route, getContextPath());
//			pageNode = js.eval_return_Map(jsCode);
//			setAttribute("PageConfig_Node", pageNode);
//		}
		
//		LOGGER.info("读取节点");
	}

	public String getName() {
		if(getNode() == null || getNode().get("name") == null) return Constant.emptyString;
		return Util.to_String(getNode().get("name"));
	}
	
	/**
	 * 读取网站结构中的某个节点。 如果网站结构中没有这个节点对象信息返回 null。
	 * 
	 * @param nodePath
	 *            节点路径，以 / 分隔，例如 /product/app
	 * @return
	 */
	public Map<String, Object> getNode(String nodePath) {
		return js.eval_return_Map(String.format("bf.AppStru.getNode('%s');", nodePath));
	}

	/**
	 * 读取节点信息，返回 hash 结构（详细信息） ${pageNode.node.name}
	 * 
	 * @return
	 */
	public Map<String, Object> getNode() {
		return getNode(getRoute());
	}
//
//	/**
//	 * 返回第几级
//	 * 
//	 * @return
//	 */
//	public int getLevels() {
//		String route = getRoute();
//		String[] arr = route.split("/");
//		return arr.length;
//	}
//
	/**
	 * @return 获取导航
	 */
	public Map<String, Object>[] getNavBar() {
		return ((ToJavaType)ConfigListener.jsRuntime).eval_return_MapArray("bf.AppStru.getNav();");
	}

	/**
	 * @return 页脚的网站地图
	 */
	public String getSiteMap() {
		String js = String.format("JSON_Tree.util.makeSiteMap(bf.AppStru.data, '%s');", getContextPath());
		return ConfigListener.jsRuntime.eval(js, String.class);
	}
 
//
//	/**
//	 * @return 获取静态资源库（js地址）
//	 */
//	public String getJsSrc() {
//		return App.isDebug ? String.format(Constant.JsSrc_local, IP.getLocalIp()) : getContextPath();
//	}
	
	/**
	 * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源（最原始的版本）
	 * @return
	 */
	public String getRoute() {
		String route = getRequestURI().replace(getContextPath(), Constant.emptyString);
		
		return route.replaceFirst("/\\w+\\.\\w+$", Constant.emptyString); // 删除 index.jsp
	}

	/**
	 * 用于 current 的对比
	 * <li
	 * ${pageContext.request.contextPath.concat('/').concat(menu.fullPath).concat('/')
	 * == pageContext.request.requestURI ? ' class=selected' : ''}> IDE
	 * 语法报错，其实正确的 于是，为了不报错 <li ${PageNode.isCurrentNode(menu) ? '
	 * class=selected' : ''}>
	 * 
	 * @param node
	 *            节点
	 * @return
	 */
	public boolean isCurrentNode(Map<String, ?> node) {
		if (node == null || node.get("fullPath") == null)
			return false;

		String fullPath = node.get("fullPath").toString(),
				ui = getContextPath().concat("/").concat(fullPath).concat("/"), uri = getRequestURI();

		return uri.equals(ui) || uri.indexOf(fullPath) != -1; //
	}
	
	private UA ua;
	
	/**
	 * 获取浏览器的 User Agent
	 * 
	 * @return User Agent
	 */
	public UA getUa() {
		return ua;
	}

	/**
	 * 保存一个浏览器的 User Agent
	 * 
	 * @param ua
	 *            User Agent
	 */
	public void setUa(UA ua) {
		this.ua = ua;
	}
	

	public Map<String, Object> getPageNode() {
		return pageNode;
	}
}