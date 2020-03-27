package com.ajaxjs.framework.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ajaxjs.config.SiteStruService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.ListMap;

/**
 * 输出页面的相关结构信息
 * 
 * @author sp42 frank@ajaxjs.com
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
	private final static String liExt = "<li%s><a href=\"%s\">%s</a><ul>%s</ul></li></li>";

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		SiteStruService sitestru = (SiteStruService) pageContext.getServletContext().getAttribute("SITE_STRU");
		if (sitestru == null)
			throw new UnsupportedOperationException(" 未 定义 SiteStruService 类型的 SITE_STRU，该常量应在 Servlet 初始化时定义。");

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String output = "Error Type for tag : ";

		switch (type) {
		case "navBar":
			output = buildNavBar(sitestru, request);
			break;
		case "secondLevelMenu":// 二级菜单
			if (request.getAttribute("PAGE_Node") != null)
				output = buildSecondLevelMenu(sitestru, request);
			break;
		case "subMenu":
			if (request.getAttribute("PAGE_Node") != null)
				output = buildSubMenu(sitestru, request);// 次级菜单
			break;
		case "breadCrumb":// 面包屑导航
			output = buildBreadCrumb(sitestru, request);
			break;
		default:
			output += type;
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
		Object _customNavLi = request.getAttribute("customNavLi");
		boolean showNavSubMenu = request.getAttribute("showNavSubMenu") != null
				&& (boolean) request.getAttribute("showNavSubMenu");
		boolean customSubMenu = false;
		String showNavSubMenuUl = null, showNavSubMenuLi = null;

		if (showNavSubMenu) {
			Object _showNavSubMenuUl = request.getAttribute("showNavSubMenuUl");
			if (_showNavSubMenuUl != null) {
				customSubMenu = true;
				showNavSubMenuUl = (String) _showNavSubMenuUl;
				showNavSubMenuLi = (String) request.getAttribute("showNavSubMenuLi");
			}
		}

		if (sitestru.getNavBar() != null) {
			for (Map<String, Object> item : sitestru.getNavBar()) {

				Object isHidden = item.get("isHidden");
				if (isHidden != null && ((boolean) isHidden) == true) // 隐藏的
					continue;

				boolean isSelected = sitestru.isCurrentNode(item, request);

				String url = ctx + "/" + item.get(ListMap.ID) + "/";
				url = addParam(url, item);

				if (_customNavLi == null)
					sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", url, item.get("name")));
				else {
					String _li = _customNavLi.toString();
					if (isSelected) {
						_li = _li.replace("class=\"", "class=\"selected ");
					}

					if (showNavSubMenu) {
						if (customSubMenu)
							sb.append(String.format(_li, url, item.get("name"),
									buildSubMenu(showNavSubMenuUl, showNavSubMenuLi, item, ctx)));
						else {
							// 默认标签的菜单
						}
					} else
						sb.append(String.format(_li, url, item.get("name")));
				}

				if (isSelected)
					hasSelected = true;
			}
		}
		if (_customNavLi == null) {
			return String.format(li, !hasSelected ? " class=\"home selected\"" : " class=\"home\"",
					"".equals(ctx) ? "/" : ctx, "首页") + sb.toString();
		} else {
			String _li = _customNavLi.toString();
			if (showNavSubMenu)
				return String.format(_li.replace("class=\"", "class=\"home "), "".equals(ctx) ? "/" : ctx, "首页", "")
						+ sb.toString();
			else
				return String.format(_li.replace("class=\"", "class=\"home "), "".equals(ctx) ? "/" : ctx, "首页")
						+ sb.toString();
		}
	}

	private static String buildSubMenu(String showNavSubMenuUl, String showNavSubMenuLi, Map<String, Object> item, String ctx) {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> menu = (List<Map<String, Object>>) item.get(ListMap.CHILDREN);

		for (Map<String, Object> m : menu) {
			String url = ctx + m.get("fullPath").toString();
			sb.append(String.format(showNavSubMenuLi, url, m.get("name").toString()));
		}

		return String.format(showNavSubMenuUl, sb.toString());
	}

	/**
	 * 
	 * @param url
	 * @param item
	 * @return
	 */
	private static String addParam(String url, Map<String, Object> item) {
		Object param = item.get("param");

		if (param != null)
			url += (String) param;

		return url;
	}

	/**
	 * 次级菜单，只是该节点下面的 children
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
			if (isHidden != null && ((boolean) isHidden) == true) // 隐藏的
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
			boolean showSubMenu = request.getAttribute("showSubMenu") != null;

			for (Map<String, Object> item : sitestru.getMenu(request)) {
				Object isHidden = item.get("isHidden");
				if (isHidden != null && ((boolean) isHidden) == true) // 隐藏的
					continue;

				String url = ctx + item.get(ListMap.PATH);
				url = addParam(url, item);

				boolean isSelected = sitestru.isCurrentNode(item, request);
				if (showSubMenu) {
					StringBuilder subMenu = new StringBuilder();
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> menu = (List<Map<String, Object>>) item.get(ListMap.CHILDREN);

					if (!CommonUtil.isNull(menu))
						for (Map<String, Object> m : menu) {
							String _url = ctx + m.get("fullPath").toString();
							subMenu.append(String.format(li, "", _url, "» " + m.get("name").toString()));
						}

					sb.append(String.format(liExt, isSelected ? " class=\"selected\"" : "", url, item.get("name"),
							subMenu.toString()));
				} else
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
		String tpl = " » <a href=\"%s\">%s</a>";

		if (node == null && request.getRequestURI().indexOf(ctx + "/index") != -1) {
			// 首页
		} else if (node != null) {
			if (node.get("supers") != null) {
				String _supers = (String) node.get("supers");
				String[] supers = _supers.split(",");

				for (String _super : supers) {
					String[] arr = _super.split(":");
					if (!CommonUtil.isNull(arr) && arr.length >= 2)
						sb.append(String.format(tpl, ctx + arr[0], arr[1]));
				}
			}

			sb.append(String.format(tpl, ctx + node.get(ListMap.PATH), node.get("name")));
		}

		sb.append("</nav>");

		// 如果有分类的话，先显示分类 （适合列表的情形）
		return sb.toString();
	}
}
