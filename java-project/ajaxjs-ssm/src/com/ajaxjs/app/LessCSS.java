package com.ajaxjs.app;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.StringUtil;

public class LessCSS {

	/**
	 * 获取本机 ip
	 * 
	 * @return
	 */
	public static String getLocalIp() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return ip == null ? "localhost" : ip;
	}

	/**
	 * 返回样式文件
	 * 
	 * @param lessPath
	 *            LESS 路径
	 * @return CSS 地址
	 */
	public static String getCssUrl(ServletContext cxt, String lessPath) {
		String css = null;

		if (ConfigListener.isDebug) {// 设置参数

			String ip = getLocalIp();
			Map<String, String> params = new HashMap<>();
			params.put("lessFile", Mappath(cxt, lessPath));
			params.put("ns", Mappath(cxt, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称

			params.put("picPath", String.format(picPath, ip, cxt.getContextPath()));// 返回
																					// CSS
																					// 背景图所用的图片

			params.put("MainDomain", "");
			params.put("isdebug", "true");

			css = "http://" + ip + "/lessService/?" + StringUtil.HashJoin(params, '&');
		} else {
			css = cxt.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
		}

		return css;
	}

	private static String Mappath(ServletContext cxt, String lessPath) {
		String absoluteAddress = cxt.getRealPath(lessPath); // 绝对地址

		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}

	// 默认 8080 端口

	private final static String picPath = "http://%s:8080/%s/asset/";

	/**
	 * 返回样式文件
	 * 
	 * @param lessPath
	 *            LESS 路径
	 * @return CSS 地址
	 */
	public static String getCssUrl(HttpServletRequest request, String lessPath) {
		String css = null;

		if (ConfigListener.isDebug) {// 设置参数

			Map<String, String> params = new HashMap<>();
			params.put("lessFile", Mappath(request, lessPath));
			params.put("ns", Mappath(request, lessPath.replaceAll("\\/[\\w\\.]*$", ""))); // 去掉文件名，保留路径，也就是文件夹名称

			params.put("picPath", getBasePath(request) + "/asset/images");// 返回
																			// CSS
																			// 背景图所用的图片

			params.put("MainDomain", "");
			params.put("isdebug", "true");

			css = "http://" + getLocalIp() + "/lessService/?" + StringUtil.HashJoin(params, '&');
		} else {
			css = request.getContextPath() + lessPath.replace("/less", "/css").replace(".less", ".css");
		}
		return css;
	}

	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String Mappath(HttpServletRequest request, String relativePath) {
		String absoluteAddress = request.getServletContext().getRealPath(relativePath); // 绝对地址

		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}

	/**
	 * 协议+主机名+端口（如果为 80 端口的话就默认不写 80）
	 * @return 网站名称
	 * 
	 */
	public static String getBasePath(HttpServletRequest request) {
		String prefix = request.getScheme() + "://" + request.getServerName();

		int port = request.getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + request.getContextPath();
	}

}
