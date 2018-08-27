package com.ajaxjs.mvc.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ajaxjs.config.SiteStruService;

public class SiteStruTag extends SimpleTagSupport {
	private String type;

	private final static String li = "<li%s><a href=\"%s\">%s</a></li>";

	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException, IOException {
		JspContext context = getJspContext();
		PageContext pageContext = (PageContext) context;
		SiteStruService sitestru = (SiteStruService) pageContext.getServletContext().getAttribute("SITE_STRU");
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String ctx = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		String output = "";

		if (sitestru == null)
			throw new UnsupportedOperationException(" 未 定义 SiteStruService 类型的 SITE_STRU，该常量应在 Servlet 初始化时定义。");

		if ("nav-bar".equals(type)) {
			boolean hasSelected = false;

			if (sitestru.getNavBar() != null) {
				for (Map<String, Object> item : sitestru.getNavBar()) {
					boolean isSelected = sitestru.isCurrentNode(item, request);
					sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", ctx + "/" + item.get("id") + "/", item.get("name")));
					if (isSelected)
						hasSelected = true;
				}
			}

			output = String.format(li, !hasSelected ? " class=\"selected\"" : "", ctx, "首页") + sb.toString();
		} else if ("secondLevelMenu".equals(type)) { // 二级菜单

			if (sitestru.getMenu(request) != null) {
				for (Map<String, Object> item : sitestru.getMenu(request)) {
					boolean isSelected = sitestru.isCurrentNode(item, request);
					sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", ctx + item.get("fullPath"), item.get("name")));
				}
			}

			output = sb.toString();

		} else if ("submenu".equals(type) && request.getAttribute("PAGE_Node") != null) { // 次级菜单
			// node 在 head.jsp 中保存
			Map<String, Object> node = (Map<String, Object>) request.getAttribute("PAGE_Node");
			List<Map<String, Object>> nodes = (List<Map<String, Object>>) node.get("children");

			for (Map<String, Object> item : nodes) {
				boolean isSelected = sitestru.isCurrentNode(item, request);
				sb.append(String.format(li, isSelected ? " class=\"selected\"" : "", ctx + item.get("fullPath"), item.get("name")));
			}

			output = sb.toString();

		} else if ("breadcrumb".equals(type) && request.getAttribute("PAGE_Node") != null) { // 面包屑导航
			sb.append(String.format("<nav class=\"anchor\">您在位置 ：<a href=\"%s\">首页 </a>", ctx));
			// MVC模式下，url 路径还是按照 JSP 的而不是 Servlet 的，我们希望统一的路径是按照 Servlet 的，故所以这里 Servlet 优先

			Map<String, Object> node = (Map<String, Object>) request.getAttribute("PAGE_Node");
			List<String> supers = (List<String>) node.get("supers");
			String tpl = "»<a href=\"%s\">%s</a>";
			for (String _super : supers) {
				String[] arr = _super.split(":");
				sb.append(String.format(tpl, ctx + arr[0], arr[1]));
			}

			sb.append(String.format(tpl, ctx + node.get("fullPath"), node.get("name")));
			sb.append("</nav>");

			// 如果有分类的话，先显示分类 （适合列表的情形）

			output = sb.toString();
		}

		context.getOut().write(output);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
