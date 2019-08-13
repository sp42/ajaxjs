package com.ajaxjs.mvc.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ajaxjs.config.SiteStruService;
import com.ajaxjs.js.ListMap;

/**
 * 输出页面的相关结构信息
 * 
 * @author Frank Cheung
 *
 */
public class SiteStruTag extends SimpleTagSupport {
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 列表标签
	 */
	private final static String li = "<li%s><a href=\"%s\">%s</a></li>";

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		SiteStruService sitestru = (SiteStruService) pageContext.getServletContext().getAttribute("SITE_STRU");
		if (sitestru == null)
			throw new UnsupportedOperationException(" 未 定义 SiteStruService 类型的 SITE_STRU，该常量应在 Servlet 初始化时定义。");
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String output = "Error Type for tag";

		if ("nav-bar".equals(type)) {
			output = buildNavBar(sitestru, request);
		} else if ("secondLevelMenu".equals(type)) { // 二级菜单
			output = buildSecondLevelMenu(sitestru, request);
		} else if ("submenu".equals(type) && request.getAttribute("PAGE_Node") != null) { // 次级菜单
			output = buildSubMenu(sitestru, request);
		} else if ("breadcrumb".equals(type) && request.getAttribute("PAGE_Node") != null) { // 面包屑导航
			output = buildBreadCrumb(sitestru, request);
		}

		pageContext.getOut().write(output);
	}
	

	/**
	 * 导航条
	 * 
	 * @param sitestru
	 * @param request
	 * @return
	 */
	private static String buildNavBar(SiteStruService sitestru, HttpServletRequest request) {
		String ctx = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		boolean hasSelected = false;

		if (sitestru.getNavBar() != null) {
			for (Map<String, Object> item : sitestru.getNavBar()) {
				
				Object isHidden = item.get("isHidden");
				if(isHidden != null && ((boolean)isHidden) == true)  // 隐藏的
					continue;
				
				boolean isSelected = sitestru.isCurrentNode(item, request);
				
				String url = ctx + "/" + item.get(ListMap.ID) + "/";
				url = addParam(url, item);			
				sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", url, item.get("name")));
				
				if (isSelected)
					hasSelected = true;
			}
		}

		return String.format(li, !hasSelected ? " class=\"selected\"" : "", "".equals(ctx) ? "/" : ctx, "首页") + sb.toString();
	}

	/**
	 * 
	 * @param url
	 * @param item
	 * @return
	 */
	private static String addParam(String url, Map<String, Object> item) {
		Object param = item.get("param");
		
		if(param != null) 
			url += (String)param;
		
		return url;
	}

	/**
	 * 次级菜单
	 * 
	 * @param sitestru
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String buildSubMenu(SiteStruService sitestru, HttpServletRequest request) {
		String ctx = request.getContextPath();
		StringBuilder sb = new StringBuilder();

		// node 在 head.jsp 中保存
		Map<String, Object> node = (Map<String, Object>) request.getAttribute("PAGE_Node");
		List<Map<String, Object>> nodes = (List<Map<String, Object>>) node.get(ListMap.CHILDREN);

		for (Map<String, Object> item : nodes) {
			Object isHidden = item.get("isHidden");
			if(isHidden!= null && ((boolean)isHidden) == true)  // 隐藏的
				continue;
			
			String url = ctx + item.get(ListMap.PATH);
			url = addParam(url, item);	
			
			boolean isSelected = sitestru.isCurrentNode(item, request);
			sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", url, item.get("name")));
		}

		return sb.toString();
	}

	/**
	 * 二级菜单
	 * 
	 * @param sitestru
	 * @param request
	 * @return
	 */
	private static String buildSecondLevelMenu(SiteStruService sitestru, HttpServletRequest request) {
		String ctx = request.getContextPath();
		StringBuilder sb = new StringBuilder();

		if (sitestru.getMenu(request) != null) {
			for (Map<String, Object> item : sitestru.getMenu(request)) {
				Object isHidden = item.get("isHidden");
				if(isHidden!= null && ((boolean)isHidden) == true)  // 隐藏的
					continue;
				
				String url = ctx + item.get(ListMap.PATH);
				url = addParam(url, item);	
				
				boolean isSelected = sitestru.isCurrentNode(item, request);
				sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", url, item.get("name")));
			}
		}

		return sb.toString();
	}

	/**
	 * 面包屑导航
	 * 
	 * @param sitestru
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String buildBreadCrumb(SiteStruService sitestru, HttpServletRequest request) {
		String ctx = request.getContextPath();
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("<nav class=\"anchor\">您的位置 ：<a href=\"%s\">首 页 </a>", ctx));
		// MVC模式下，url 路径还是按照 JSP 的而不是 Servlet 的，我们希望统一的路径是按照 Servlet 的，故所以这里 Servlet 优先

		Map<String, Object> node = (Map<String, Object>) request.getAttribute("PAGE_Node");
		List<String> supers = (List<String>) node.get("supers");
		String tpl = " » <a href=\"%s\">%s</a>";

		for (String _super : supers) {
			String[] arr = _super.split(":");
			sb.append(String.format(tpl, ctx + arr[0], arr[1]));
		}

		sb.append(String.format(tpl, ctx + node.get(ListMap.PATH), node.get("name")));
		sb.append("</nav>");

		// 如果有分类的话，先显示分类 （适合列表的情形）

		return sb.toString();
	}
}
