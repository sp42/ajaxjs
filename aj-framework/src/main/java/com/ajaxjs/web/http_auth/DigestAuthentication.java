package com.ajaxjs.web.http_auth;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.io.StreamHelper;

public class DigestAuthentication implements Filter {

	/**
	 * 用户名，你可以改为你配置的
	 */
	private String userName = "usm";

	/**
	 * 密码，你可以改为你配置的
	 */
	private String password = "password";

	/**
	 * 
	 */
	private String authMethod = "auth";

	/**
	 * 
	 */
	private String realm = "example.com";

	public String nonce;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("HTTP DigestAuthentication……");

		// 定时器，每分钟刷新 nonce
		nonce = calculateNonce();

//		SetTimeout.timeout(() -> {
//			System.out.println("刷新 Nonce....");
////			log("刷新 Nonce....");
//			nonce = calculateNonce();
//		}, 1, 1);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
		authenticate((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	@Override
	public void destroy() {
	}

	protected void authenticate(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException {
		resp.setContentType("text/html;charset=UTF-8");
		String authHeader = req.getHeader("Authorization");

		try {
			if (StringUtils.hasText(authHeader)) {
				if (authHeader.startsWith("Digest")) {
					// parse the values of the Authentication header into a hashmap
					Map<String, String> headerValues = parseHeader(authHeader);
					String method = req.getMethod();
					String ha1 = StrUtil.md5(userName + ":" + realm + ":" + password);
					String ha2;
					String qop = headerValues.get("qop");
					String reqURI = headerValues.get("uri");

					if (StringUtils.hasText(qop) && qop.equals("auth-int")) {
						String requestBody = "";
						try (InputStream in = req.getInputStream()) {
							StreamHelper.byteStream2string(in);
						}

						String entityBodyMd5 = StrUtil.md5(requestBody);
						ha2 = StrUtil.md5(method + ":" + reqURI + ":" + entityBodyMd5);
					} else
						ha2 = StrUtil.md5(method + ":" + reqURI);

					String serverResponse;

					if (StringUtils.hasText(qop)) {
//						String domain = headerValues.get("realm");
						String nonceCount = headerValues.get("nc");
						String clientNonce = headerValues.get("cnonce");

						serverResponse = StrUtil.md5(ha1 + ":" + nonce + ":" + nonceCount + ":" + clientNonce + ":" + qop + ":" + ha2);
					} else
						serverResponse = StrUtil.md5(ha1 + ":" + nonce + ":" + ha2);

					String clientResponse = headerValues.get("response");

					if (!serverResponse.equals(clientResponse)) {
						show401(resp);
						return;
					}
				} else {
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, " This Servlet only supports Digest Authorization");
					return;
				}
			} else {
				show401(resp);
				return;
			}

			// allows to go
			chain.doFilter(req, resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void show401(HttpServletResponse resp) throws IOException {
		resp.addHeader("WWW-Authenticate", getAuthenticateHeader());
		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * 解析 Authorization 头，将其转换为一个 Map
	 * Gets the Authorization header string minus the "AuthType" and returns a
	 * hashMap of keys and values
	 *
	 * @param header
	 * @return
	 */
	private static Map<String, String> parseHeader(String header) {
		// seperte out the part of the string which tells you which Auth scheme is it
		String headerWithoutScheme = header.substring(header.indexOf(" ") + 1).trim();
		String keyValue[] = headerWithoutScheme.split(",");
		Map<String, String> values = new HashMap<>();

		for (String keyval : keyValue) {
			if (keyval.contains("=")) {
				String key = keyval.substring(0, keyval.indexOf("="));
				String value = keyval.substring(keyval.indexOf("=") + 1);
				values.put(key.trim(), value.replaceAll("\"", "").trim());
			}
		}

		return values;
	}

	/**
	 * 生成认证的 HTTP 头
	 * 
	 * @return
	 */
	private String getAuthenticateHeader() {
		String header = "";

		header += "Digest realm=\"" + realm + "\",";
		if (StringUtils.hasText(authMethod))
			header += "qop=" + authMethod + ",";

		header += "nonce=\"" + nonce + "\",";
		header += "opaque=\"" + StrUtil.md5(realm + nonce) + "\""; // 域名跟 nonce 的 md5 = Opaque

		return header;
	}

	/**
	 * 根据时间和随机数生成 nonce
	 * 
	 * Calculate the nonce based on current time-stamp upto the second, and a random seed
	 *
	 * @return
	 */
	public static String calculateNonce() {
		String now = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss").format(new Date());

		return StrUtil.md5(now + new Random(100000).nextInt());
	}
}
