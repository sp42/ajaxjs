package com.ajaxjs.util.websecurity.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.util.websecurity.ISecurityFilter;
import com.ajaxjs.util.websecurity.SecurityConstant;

/**
 * 只允许表单post提交
 * 
 * @author weijian.zhongwj
 * 
 */
public class FormPostPermitCheckFilter implements ISecurityFilter {

	@Override
	public void doFilterInvoke(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		if (!Valid(request.getRequestURI(), request.getMethod())) {
			throw new RuntimeException("this requestUrI : "
					+ request.getRequestURI()
					+ " only permit post, but now is " + request.getMethod());
		}
	}

	private boolean Valid(String requestURI, String method) {
		if (!"POST".equalsIgnoreCase(method)) {
			for (String patternUri : SecurityConstant.onlyPostUrlList) {
				if (Pattern.matches(patternUri, requestURI))
					return false;
			}
		}
		return true;
	}

}
