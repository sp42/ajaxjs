package com.ajaxjs.web.security.in_out_filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ajaxjs.web.security.ConfigLoader;

/**
 * @author Frank
 *
 */
public class XSS_Filter_Response extends HttpServletResponseWrapper {
	public XSS_Filter_Response(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(name, XSS_Filter_Request.xssFilter(value, null));
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, XSS_Filter_Request.xssFilter(value, null));
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		if (!checkRedirectValid(location)) {
			throw new RuntimeException("跳转地址 " + location + " 非法，因为不在白名单中。");
		}
		super.sendRedirect(location);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, XSS_Filter_Request.xssFilter(sm, null));
	}

	private static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty())
			return false;

		for (Pattern pattern : ConfigLoader.redirectLocationWhiteList) {
			if (pattern.matcher(location).find())
				return true;
		}

		return false;
	}
}
