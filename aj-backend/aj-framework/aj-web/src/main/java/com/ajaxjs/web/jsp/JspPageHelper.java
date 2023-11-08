//package com.ajaxjs.web.jsp;
//
//
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 传统分页
// * 
// * @author Frank Cheung sp42@qq.com
// *
// */
//public class JspPageHelper {
//	private PageResult<?> pageResult;
//
//	private HttpServletRequest request;
//
//	public void setRequest(HttpServletRequest request) {
//		this.request = request;
//	}
//
//	private static final String EMPTY_STR = "";
//
//	private static final String PER = "<a href=\"%s\">上一页</a>";
//
//	/**
//	 * 上一页的连接
//	 * 
//	 * @return
//	 */
//	public String getPerLink() {
//		if (pageResult.getTotalCount() > 0 && pageResult.getStart() > 0) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("?start=" + (pageResult.getStart() - pageResult.getPageSize()));
//			sb.append(withoutParam("start"));
//
//			return String.format(PER, sb.toString());
//		} else
//			return EMPTY_STR;
//	}
//
//	/**
//	 * 对某段 URL 参数剔除其中的一个
//	 * 通常由 request.getQueryString() 或 ${pageContext.request.queryString} 返回的 url 参数
//	 * 
//	 * @param param 不需要的那个参数
//	 * @return
//	 */
//	private String withoutParam(String param) {
//		String queryString = request.getQueryString();
//
//		if (!StringUtils.hasText(queryString))
//			return EMPTY_STR;
//
//		queryString = queryString.replaceAll("&?" + param + "=[^&]*", "");// 删除其中一个参数
//
//		queryString = queryString.startsWith("&") ? queryString : "&" + queryString; // 补充关联的符号
//
//		if ("&".equals(queryString))
//			queryString = EMPTY_STR;
//
//		return queryString;
//	}
//
//	private static final String NEXT = "<a href=\"%s\">下一页</a>";
//
//	/**
//	 * 下一页的连接
//	 * 
//	 * @return
//	 */
//	public String getNextLink() {
//		if (pageResult.getStart() + pageResult.getPageSize() < pageResult.getTotalCount()) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("?start=" + (pageResult.getStart() + pageResult.getPageSize()));
//			sb.append(withoutParam("start"));
//
//			return String.format(NEXT, sb);
//		} else
//			return EMPTY_STR;
//	}
//
//	public String getInfo() {
//		String info = "页数：" + pageResult.getCurrentPage() + "/" + pageResult.getTotalPage();
//		info += "记录数：" + (pageResult.getStart() + pageResult.getPageSize()) + "/" + pageResult.getTotalCount();
//
//		return info;
//
//	}
//
//	public PageLink[] getLink() {
//		PageResult<?> p = (PageResult<?>) request.getAttribute("PAGE_RESULT");
//		PageLink[] arr = new PageLink[p.getTotalPage()];
//		String withoutParam = withoutParam("start");
//
//		for (int i = 0; i < p.getTotalPage(); i++) {
//			PageLink pl = new PageLink();
//			int j = i + 1;
//			pl.setPageNo(j);
//			pl.setUrl("?start=" + (i * pageResult.getPageSize()) + withoutParam);
//			pl.setSelected(j == pageResult.getCurrentPage());
//
//			arr[i] = pl;
//		}
//
//		request.setAttribute("PAGE_LINK", arr);
//
//		return arr;
//	}
//
//	public static class PageLink {
//		private String url;
//
//		private int pageNo;
//
//		private boolean isSelected;
//
//		public int getPageNo() {
//			return pageNo;
//		}
//
//		public void setPageNo(int pageNo) {
//			this.pageNo = pageNo;
//		}
//
//		public boolean isSelected() {
//			return isSelected;
//		}
//
//		public void setSelected(boolean isSelected) {
//			this.isSelected = isSelected;
//		}
//
//		public String getUrl() {
//			return url;
//		}
//
//		public void setUrl(String url) {
//			this.url = url;
//		}
//	}
//
//	public PageResult<?> getPageResult() {
//		return pageResult;
//	}
//
//	public void setPageResult(PageResult<?> pageResult) {
//		this.pageResult = pageResult;
//	}
//
//}
