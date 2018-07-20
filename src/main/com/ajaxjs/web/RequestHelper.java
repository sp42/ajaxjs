package com.ajaxjs.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestHelper extends HttpServletRequestWrapper {
	public RequestHelper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 获取磁盘真實地址。
	 * 
	 * @param cxt
	 *            Web 上下文
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String mappath(ServletContext cxt, String relativePath) {
		String absolute = cxt.getRealPath(relativePath);

		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
	}

	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public String mappath(String relativePath) {
		return mappath(getServletContext(), relativePath);
	}

	/**
	 * 返回协议+主机名+端口（如果为 80 端口的话就默认不写 80）
	 * 
	 * @return 网站名称
	 */
	public String getBasePath() {
		String prefix = getScheme() + "://" + getServerName();

		int port = getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + "/" + getContextPath();
	}

	/**
	 * 获取请求 ip
	 * 
	 * @param request
	 *            请求对象
	 * @return 客户端 ip
	 */
	public String getIp() {
		String ip = getHeader("x-forwarded-for");

		if (!"unknown".equalsIgnoreCase(ip) && ip != null && ip.length() != 0) {
			int index = ip.indexOf(",");
			if (index != -1)
				ip = ip.substring(0, index);

			return ip;
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getHeader("Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getHeader("WL-Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getHeader("X-Real-Ip");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getRemoteAddr();

		return ip;
	}
}
