/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.security.filter;

import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author Frank
 *
 */
public class EncrySessionInCookie {
	private static final String sessionCookieName = "tmp_app";

	private static final char sep = (char) 1;
	private static final char sep2 = (char) 2;

	public static String key;

	public static void deseriable(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return;
		}

		for (Cookie cookie : cookies) {// 遍历 cookie
			if (cookie.getName().equals(sessionCookieName)) {
				try {
					String value = decrypt(cookie.getValue(), key);
					String[] kvs = value.split(sep2 + "");
					if (kvs == null || kvs.length == 0) {
						return;
					}
					for (String kv : kvs) {
						String[] param = kv.split(sep + "");
						if (param == null || param.length == 0 || param.length != 2) {
							continue;
						}
						request.getSession().setAttribute(param[0], param[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把 session 所有东西保存到 cookies
	 * @param request
	 * @param response
	 * @param key
	 */
	public static void seriable(HttpServletRequest request, HttpServletResponse response, String key) {
		StringBuilder sb = new StringBuilder();
		HttpSession session = request.getSession();
		Enumeration<String> enums = session.getAttributeNames();

		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			sb.append(name + sep + session.getAttribute(name) + sep2);
		}

		String encrypt = null;

		try {
			encrypt = encrypt(sb.toString(), key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.addCookie(new Cookie(sessionCookieName, encrypt));
	}

	public static String encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null || sKey.length() != 16) {
			return null;
		}

		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(sSrc.getBytes());

		return new BASE64Encoder().encode(encrypted);
	}

	public static String decrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null || sKey.length() != 16)
			return null;

		try {
			byte[] raw = sKey.getBytes("ASCII");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(raw, "AES"),
					new IvParameterSpec("0102030405060708".getBytes()));

			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc), original = cipher.doFinal(encrypted1);

			return new String(original);
		} catch (Exception e) {
			throw e;
		}
	}
}
