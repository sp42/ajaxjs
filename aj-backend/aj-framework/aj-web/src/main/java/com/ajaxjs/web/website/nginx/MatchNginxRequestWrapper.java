package com.ajaxjs.web.website.nginx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MatchNginxRequestWrapper extends HttpServletRequestWrapper {
	public MatchNginxRequestWrapper(HttpServletRequest request) {
		super(request);
		oldContextPath = request.getContextPath();
		oldRequestURI = request.getRequestURI();
	}

	private final String oldContextPath;

	private final String oldRequestURI;

	@Override
	public String getContextPath() {
		String unknown = "unknown";
		// 先从nginx自定义配置获取
		String contextPath = super.getContextPath();
		String temp = getHeader("Context-Path");

		if (temp != null && temp.length() > 0 && !unknown.equalsIgnoreCase(temp))
			contextPath = temp;

		if ("/".equals(contextPath))
			return "";

		return contextPath;
	}

	@Override
	public String getRequestURI() {
		// 把requestURI 的 oldContextPath 部分替换成 新的 contextPath
		String requestURI = super.getRequestURI();

		if (requestURI != null && requestURI.startsWith(oldContextPath))
			requestURI = getContextPath() + requestURI.substring(oldContextPath.length());

		return requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		// 把 RequestURL 的 oldRequestURI 部分替换成 新的 oldRequestURI
		StringBuffer requestURL = super.getRequestURL();

		if (requestURL != null) {
			String toString = requestURL.toString();

			if (toString.endsWith(oldRequestURI))
				requestURL.replace(toString.indexOf(oldRequestURI),
						toString.indexOf(oldRequestURI) + oldRequestURI.length(), getRequestURI());
		}

		return requestURL;
	}

	@Override
	public String getScheme() {
		String unknown = "unknown";
		// 先从nginx自定义配置获取
		String scheme = super.getScheme();
		String temp = getHeader("X-Forwarded-Proto");

		if (temp != null && temp.length() > 0 && !unknown.equalsIgnoreCase(temp))
			scheme = temp;

		return scheme;
	}

	@Override
	public int getServerPort() {
		String unknown = "unknown";
		// 先从nginx自定义配置获取
		int serverPort = super.getServerPort();
		String temp = getHeader("Port");

		if (temp != null && temp.length() > 0 && !unknown.equalsIgnoreCase(temp))
			serverPort = Integer.parseInt(temp);

		return serverPort;
	}

}
