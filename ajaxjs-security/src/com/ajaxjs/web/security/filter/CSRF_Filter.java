package com.ajaxjs.web.security.filter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 对 POST 表单提交进行 CSRF token 验证； 使用 getCsrfTokenId() 然后放入表单还有 session 中，key 名称必须为
 * csrf_ 开头； 为了支持多个form表单
 * 
 * @author Frank
 *
 */
public class CSRF_Filter extends SimpleFilter {
	private static final String CSRFTOKEN_PREFIX = "csrf_";

	@Override
	public boolean check(HttpServletRequest request) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return true;
		}

		String tokenKey = null;// 获取 csrf_ 开头的 key

//		Iterator<Entry<String, String[]>> iter = request.getParameterMap().entrySet().iterator();
//		while (iter.hasNext()) {
//			Entry<String, String[]> entry = iter.next();
//			if (entry.getKey().startsWith(CSRFTOKEN_PREFIX))
//				tokenKey = entry.getKey();
//		}
		
		for (String key : request.getParameterMap().keySet()) {
			if (key.startsWith(CSRFTOKEN_PREFIX)) {
				tokenKey = key;
				break;
			}
		}

		if (tokenKey == null) // 找不到
			return false;

		long csrfTokenId = (Long) request.getSession().getAttribute(tokenKey),
				paramCsrfToken = Long.parseLong(request.getParameter(tokenKey));

		if (csrfTokenId != paramCsrfToken)
			throw new SecurityException("post method csrf token not valid.");

		return false;
	}

	/**
	 * 生成 CSRF token
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
}
