package com.ajaxjs.util.websecurity.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ajaxjs.util.websecurity.ISecurityFilter;

public class CsrfTokenCkeckFilter implements ISecurityFilter {

	private static final String CSRFTOKEN_PREFIX = "csrf_";

	@Override
	public void doFilterInvoke(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
		if (httpRequest.getMethod().equals("POST")) {
			String csrfTokenKey = getTokenName(httpRequest);
			long csrfTokenId = (Long) httpRequest.getSession().getAttribute(csrfTokenKey);
			long paramCsrfToken = Long.parseLong(httpRequest.getParameter(csrfTokenKey));
			if (csrfTokenId != paramCsrfToken) 
				throw new RuntimeException("post method csrf token not valid.");
		}
	}

	private String getTokenName(HttpServletRequest httpRequest) {
		Iterator<Entry<String, String[]>> iter = httpRequest.getParameterMap().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			if (entry.getKey().startsWith(CSRFTOKEN_PREFIX))
				return entry.getKey();
		}
		
		return null;
	}
	
	/**
	 * @deprecated
	 *
	 */
	public static class CsrfTokenIdCreator {

		public String getNextToken(HttpSession session) {
			try {
				return new String(MessageDigest.getInstance("MD5").digest((session.getCreationTime() + session.getId()).getBytes()));
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
}


