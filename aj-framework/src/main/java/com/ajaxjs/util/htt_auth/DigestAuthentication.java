package com.ajaxjs.util.htt_auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import org.springframework.util.DigestUtils;

import com.ajaxjs.util.SetTimeout;
import com.ajaxjs.util.io.StreamHelper;

public class DigestAuthentication implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		authenticate(request, response);
	}

	@Override
	public void destroy() {
	}
	
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

	private static final long serialVersionUID = 1L;

	/**
	 * 定时器，每分钟刷新 nonce
	 */
	public TestController2() {
		nonce = calculateNonce();

		SetTimeout.timeout(() -> {
//			log("刷新 Nonce....");
			nonce = calculateNonce();
		}, 1, 1);
	}

	protected void authenticate(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/html;charset=UTF-8");
		String authHeader = req.getHeader("Authorization");

		try (PrintWriter out = resp.getWriter();) {
			if (isBlank(authHeader)) {
				resp.addHeader("WWW-Authenticate", getAuthenticateHeader());
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				if (authHeader.startsWith("Digest")) {
					// parse the values of the Authentication header into a hashmap
					Map<String, String> headerValues = parseHeader(authHeader);
					String method = req.getMethod();
					String ha1 = md5Hex(userName + ":" + realm + ":" + password);
					String ha2;
					String qop = headerValues.get("qop");
					String reqURI = headerValues.get("uri");

					if (!isBlank(qop) && qop.equals("auth-int")) {
						String requestBody = "";
						try (InputStream in = req.getInputStream()) {
							StreamHelper.byteStream2string(in);
						}

						String entityBodyMd5 = md5Hex(requestBody);
						ha2 = md5Hex(method + ":" + reqURI + ":" + entityBodyMd5);
					} else
						ha2 = md5Hex(method + ":" + reqURI);

					String serverResponse;

					if (isBlank(qop))
						serverResponse = md5Hex(ha1 + ":" + nonce + ":" + ha2);
					else {
//						String domain = headerValues.get("realm");
						String nonceCount = headerValues.get("nc");
						String clientNonce = headerValues.get("cnonce");

						serverResponse = md5Hex(ha1 + ":" + nonce + ":" + nonceCount + ":" + clientNonce + ":" + qop + ":" + ha2);
					}

					String clientResponse = headerValues.get("response");

					if (!serverResponse.equals(clientResponse)) {
						resp.addHeader("WWW-Authenticate", getAuthenticateHeader());
						resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					}
				} else
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, " This Servlet only supports Digest Authorization");
			}

			out.println("<head>");
			out.println("<title>Servlet HttpDigestAuth</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>已通过 HttpDigestAuth 认证 at" + req.getContextPath() + "</h1>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String md5Hex(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
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
		if (!isBlank(authMethod))
			header += "qop=" + authMethod + ",";

		header += "nonce=\"" + nonce + "\",";
		header += "opaque=\"" + getOpaque(realm, nonce) + "\"";

		return header;
	}

	private boolean isBlank(String str) {
		return str == null || "".equals(str);
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

		return md5Hex(now + new Random(100000).nextInt());
	}

	/**
	 * 域名跟 nonce 的 md5 = Opaque
	 * 
	 * @param domain
	 * @param nonce
	 * @return
	 */
	private static String getOpaque(String domain, String nonce) {
		return md5Hex(domain + nonce);
	}
}
