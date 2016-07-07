package com.ajaxjs.web.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.ajaxjs.web.security.filter.XSS;

public class SecurityRequest extends HttpServletRequestWrapper {

	public SecurityRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		name = XSS.xssFilter(name, XSS.XssFilterTypeEnum.DELETE.getValue());
		return XSS.xssFilter(super.getParameter(name), null);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramsMap = super.getParameterMap();
		if (paramsMap == null)
			return null;

		Map<String, String[]> resParamsMap = new HashMap<>();
		Iterator<Entry<String, String[]>> iter = paramsMap.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			resParamsMap.put((XSS.xssFilter(entry.getKey(), XSS.XssFilterTypeEnum.DELETE.getValue())),
					filterList(entry.getValue()));
		}

		return resParamsMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vec = new Vector<>();

		while (enums.hasMoreElements()) {
			String value = enums.nextElement();
			vec.add(XSS.xssFilter(value, null));
		}

		return vec.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		return filterList(super.getParameterValues(name));
	}

	private String[] filterList(String[] value) {
		if (value == null || value.length == 0)
			return null;

		List<String> resValueList = new ArrayList<>();

		for (String val : value)
			resValueList.add(XSS.xssFilter(val, null));

		return resValueList.toArray(new String[resValueList.size()]);
	}

	/**
	 * 文件上传安全过滤
	 */
	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		Collection<Part> parts = super.getParts();

		if (parts == null || parts.isEmpty() || SecurityFilter.whitefilePostFixList == null
				|| SecurityFilter.whitefilePostFixList.isEmpty()) {
			return parts;
		}

		List<Part> resParts = new ArrayList<>();
		for (Part part : parts) {
			for (String extension : SecurityFilter.whitefilePostFixList) {
				if (part.getName().toUpperCase().endsWith(extension))
					resParts.add(part);

			}
		}

		return resParts;
	}

	/**
	 * 上传文件后缀名符合白名单则允许上传
	 */
	@Override
	public Part getPart(String name) throws IOException, ServletException {
		Part part = super.getPart(name);

		if (SecurityFilter.whitefilePostFixList.isEmpty()) {
			return part;
		}

		// String value = part.getHeader("content-disposition");
		// String filename = value.substring(value.lastIndexOf("=") + 2,
		// value.length() - 1);
		String filename = part.getName();

		for (String extension : SecurityFilter.whitefilePostFixList) {
			if (filename.toUpperCase().endsWith(extension.toUpperCase()))
				return part;
		}

		return null;
	}

	/**
	 * 返回符合白名单的 cookie 输入输出cookie白名单验证过滤
	 */
	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();

		if (cookies == null || cookies.length == 0)
			return null;

		List<Cookie> cookieList = new ArrayList<>();
		for (Cookie cookie : cookies) {
			for (String name : SecurityFilter.cookieWhiteList) {
				if (name.equalsIgnoreCase(cookie.getName()))
					cookieList.add(cookie);
			}
		}

		return cookieList.toArray(new Cookie[cookieList.size()]);
	}

	//////////////////////////////////////////////////////////////////////////////

	/**
	 * 禁止非法 POST 请求。 只允许 post 提交的 url 列表，需要配置 onlyPostUrlList 参数
	 * 
	 * @throws SecurityException
	 */
	public void formPostPermitCheck() throws SecurityException {
		if (!Valid(getRequestURI(), getMethod())) {
			String msg = "请求地址 %s 禁止 POST 操作";
			throw new SecurityException(String.format(msg, getRequestURI()));
		}
	}

	private static boolean Valid(String requestURI, String method) {
		if (!POST.equalsIgnoreCase(method)) {
			for (String patternUri : SecurityFilter.onlyPostUrlList) {
				if (Pattern.matches(patternUri, requestURI))
					return false;
			}
		}
		return true;
	}

	//////////////////////////////////////////////////////////////////////////////

	/**
	 * 对post表单提交进行csrf token验证； 使用CsrfTokenIdCreator生成csrf
	 * tokenid后放入表单还有session中，key名称必须为csrf_开头； 为了支持多个form表单
	 */
	private static final String CSRFTOKEN_PREFIX = "csrf_";
	private static final String POST = "POST";

	public void checkCsrfToken() throws SecurityException {
		if (getMethod().equals(POST)) {
			String csrfTokenKey = getTokenName();

			long csrfTokenId = (Long) getSession().getAttribute(csrfTokenKey),
					paramCsrfToken = Long.parseLong(getParameter(csrfTokenKey));

			if (csrfTokenId != paramCsrfToken)
				throw new SecurityException("post method csrf token not valid.");
		}
	}

	/**
	 * 获取 csrf_开头的 key
	 * 
	 * @return
	 */
	private String getTokenName() {
		Iterator<Entry<String, String[]>> iter = getParameterMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			if (entry.getKey().startsWith(CSRFTOKEN_PREFIX))
				return entry.getKey();
		}

		return null;
	}

	/**
	 * 使用CsrfTokenIdCreator生成csrf tokenid后放入表单
	 * 
	 * @param session
	 * @return
	 */
	public static String getCsrfTokenId(HttpSession session) {
		String str = session.getCreationTime() + session.getId();

		try {
			return new String(MessageDigest.getInstance("MD5").digest(str.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	//////////////////////////////////////////////////////////////////////////////

	/**
	 * 利用来路 Referer 请求头阻止"盗链"
	 * 
	 * @param request
	 * @param site
	 *            本站站名，应以 http 开头
	 * @return true 表示为同域
	 */
	public boolean isSameDomain(String site) {
		String _referer = getHeader("referer");
		if (_referer == null || "".equals(_referer))
			return false;

		if (site == null)
			site = "http://" + getServerName();
		return _referer.startsWith(site);
	}

	/**
	 * 一种表单重复提交处理方法 http://blog.csdn.net/5iasp/article/details/4268710 判断是否为重复提交
	 * 1，检查Session中是否含有指定名字的属性 2，如果Session中没有该属性或者属性为空，证明已被处理过，判断为重复提交
	 * 3，否则，证明是第一次处理，并将属性从Session中删除。 1. 在生成表单时执行如下: //
	 * session.setAttribute("forum_add", "forum_add"); 2. 提交处理时作如下判断 // if
	 * (isRedo(request, "forum_add")) { // //提示重复提交,作相关处理 // }
	 * 
	 * 《JSP 防止重复提交方法 》 http://blog.csdn.net/seablue_xj/article/details/4934367
	 * 《javaEE开发中使用session同步和token机制来防止并发重复提交》
	 * http://www.iflym.com/index.php/code/avoid-conrrent-duplicate-submit-by-
	 * use-session-synchronized-and-token.html
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public boolean isRedo(String key) {
		Object obj = getSession().getAttribute(key);
		if (obj == null) {
			return true;
		} else {
			getSession().removeAttribute(key);
			return false;
		}
	}
}
