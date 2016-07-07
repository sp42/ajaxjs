package com.ajaxjs.web.security;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.AesCipherService;

/**
 * session信息保存在cookie中
 * 
 * @author weijian.zhongwj
 *
 */
public class HttpSessionCookieStore {

	private static final String sessionCookieName = "tmp_app";

	private static final char sep = (char) 1;
	private static final char sep2 = (char) 2;

	private HttpServletRequest httpServletRequest;

	private HttpServletResponse httpServletResponse;

	private String key;

	/**
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param key
	 */
	public HttpSessionCookieStore(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String key) {
		this.httpServletRequest = httpServletRequest;
		this.httpServletResponse = httpServletResponse;
		this.key = key;
	}

	/**
	 * 从 cookie 中取出值放在 session 中
	 * @param session
	 */
	public void deseriable(HttpSession session) {
		Cookie[] cookies = httpServletRequest.getCookies();
		if (!Util.isNotNull(cookies)) {
			return;
		}

		AesCipherService aesCipherService = new AesCipherService();
		aesCipherService.setKeySize(64); // 设置key长度

		for (Cookie cookie : cookies) {// 遍历 cookie
			if (cookie.getName().equals(sessionCookieName)) {
				try {
					String value = aesCipherService.encrypt(cookie.getValue().getBytes(), key.getBytes()).toHex();
					String[] kvs = value.split(sep2 + "");
					
					if (!Util.isNotNull(kvs)) {
						return;
					}
					
					for (String kv : kvs) {
						String[] param = kv.split(sep + "");
						if (!Util.isNotNull(param) || param.length != 2) {
							continue;
						}
						session.setAttribute(param[0], param[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把 session 所有东西保存到 cookies
	 * @param session
	 */
	public void seriable(HttpSession session) {
		StringBuilder sb = new StringBuilder();
		
		Enumeration<String> enums = session.getAttributeNames();
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			sb.append(name + sep + session.getAttribute(name) + sep2);
		}

		AesCipherService aesCipherService = new AesCipherService();
		aesCipherService.setKeySize(64); // 设置key长度

		String str = aesCipherService.encrypt(sb.toString().getBytes(), key.getBytes()).toHex();
		httpServletResponse.addCookie(new Cookie(sessionCookieName, str));
	}

}
