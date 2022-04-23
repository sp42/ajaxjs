package com.ajaxjs.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * Web 工具类
 *
 * @author Frank Cheung
 */
public class WebHelper {
	public static String uriDecode(String str) {
		return UriUtils.decode(str, "utf-8");
	}

	/**
	 * 获取请求 ip
	 *
	 * @return 客户端 ip
	 */
	public static String getIp(HttpServletRequest req) {
		String ip = req.getHeader("x-forwarded-for");

		if (!"unknown".equalsIgnoreCase(ip) && ip != null && ip.length() != 0) {
			int index = ip.indexOf(",");

			if (index != -1)
				ip = ip.substring(0, index);

			return ip.startsWith("::ffff:") ? ip.replaceAll("::ffff:", "") : ip;
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = req.getHeader("Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = req.getHeader("WL-Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = req.getHeader("X-Real-Ip");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = req.getRemoteAddr();

		return ip.startsWith("::ffff:") ? ip.replaceAll("::ffff:", "") : ip;
	}

	/**
	 * 获取指定 Cookie 的值
	 *
	 * @param key Cookie 的 key
	 * @return Cookie 的值
	 */
	public static String getCookie(HttpServletRequest req, String key) {
		Cookie[] cookieArr = req.getCookies();

		if (!ObjectUtils.isEmpty(cookieArr)) {
			for (Cookie cookie : cookieArr) {
				if (key.equals(cookie.getName()))
					return cookie.getValue();
			}
		}

		return null;
	}

	/**
	 * 获取磁盘真實地址
	 *
	 * @param cxt          Web 上下文
	 * @param relativePath 相对地址
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
	 * @param relativePath 相对地址，例如 /images
	 * @return 绝对地址
	 */
	public static String mappath(HttpServletRequest req, String relativePath) {
		return mappath(req.getServletContext(), relativePath);
	}

	/**
	 * 返回协议+主机名+端口+项目前缀（如果为 80 端口的话就默认不写 80） 有时通过代理返回的，可能不正确
	 *
	 * @return 网站名称
	 */
	public static String getBasePath(HttpServletRequest req) {
		String prefix = req.getScheme() + "://" + req.getServerName();

		int port = req.getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + req.getContextPath();
	}

	/**
	 * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源（最原始的版本）
	 *
	 * @return 请求路径
	 */
	public static String getRoute(HttpServletRequest req) {
		String route = req.getRequestURI().replaceAll("^" + req.getContextPath(), "");

		LogHelper.p(route);
		LogHelper.p(route.replaceFirst("/\\w+\\.\\w+$", ""));

		return route.replaceFirst("/\\w+\\.\\w+$", ""); // 删除最后的 index.jsp
	}

	/**
	 * PUT + -x-www-form-urlencoded 不能直接 参数 转 Map/Bean。 手动转换又与德金的框架冲突， 但幸运的是可以通过
	 * parameterMap() 获取（这在过去的是不行的） 于是考虑这个两种兼容的方式
	 *
	 * @param req
	 * @return
	 */
	public static Map<String, Object> getParameterMap(HttpServletRequest req) {
		Map<String, Object> map;
		Map<String, String[]> parameterMap = req.getParameterMap();

		if (parameterMap.size() > 0) {
			// 德金的可以
//			map = MapTool.as(parameterMap, arr -> MappingValue.toJavaValue(arr[0]));
			map = MapTool.as(parameterMap);
		} else {
			// 旧的方式
			return getPutRequestData(req);
		}

		return map;
	}

	public static String getRawBody(HttpServletRequest req) {
		try (InputStream in = req.getInputStream()) {
			return StreamHelper.byteStream2string(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取 RawBody 内容，序列化为 JSON 返回
	 *
	 * @return 参数、值集合
	 */
	public static Map<String, Object> getRawBodyAsJson(HttpServletRequest req) {
		return JsonHelper.parseMap(getRawBody(req));
	}

	/**
	 * 获取 PUT 请求所提交的内容。 Servlet 没有 PUT 获取表单，要自己处理 Servlet 不能获取 PUT 请求内容，spring mvc
	 * 也不支持 put form data
	 *
	 * @return 参数、值集合
	 */
	public static Map<String, Object> getPutRequestData(HttpServletRequest req) {
		try (InputStream in = req.getInputStream()) {
			String params = StreamHelper.byteStream2string(in);
			return MapTool.toMap(params.split("&"), v -> MappingValue.toJavaValue(StringUtils.uriDecode(v, StandardCharsets.UTF_8)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 支持自动获取请求参数并封装到 Bean 内
	 *
	 * @param req
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public static <T> T getParameterBean(HttpServletRequest req, Class<T> clz) {
		/*
		 * 抛出 IllegalArgumentException 这个异常 有可能是参数类型不一致造成的， 要求的是 string 因为 map 从 request
		 * 所以最后一个参数为 true
		 */
		return MapTool.map2Bean(getParameterMap(req), clz, true);
	}

	/**
	 * 获取所有 QueryString 参数，并转化为真实的值，最后返回一个 Map
	 *
	 * @param request 请求对象
	 * @return 所有 QueryString 参数
	 */
	public static Map<String, Object> getQueryParameters(HttpServletRequest request) {
		String queryString = request.getQueryString();

		if (!StringUtils.hasText(queryString))
			return null;

		String[] parameters = queryString.split("&");
		Map<String, Object> map = new HashMap<>();

		for (String parameter : parameters) {
			String[] keyValuePair = parameter.split("=");
			String v = keyValuePair[1];
			v = uriDecode(v);

			map.put(keyValuePair[0], keyValuePair.length == 1 ? "" : MappingValue.toJavaValue(v));
		}

		return map;
	}

	/**
	 * 提示 HTML 模板
	 */
	public static final String HTML_TPL = "<title>操作错误</title>" + "<meta charset=\"utf-8\" />"
			+ "<div style=\"height: 100%%; display: flex; justify-content: center; align-items: center;\"><table>" + "		<tr><td align=\"center\">"
			+ "			<svg width=\"150px\" viewBox=\"0 0 1000 1000\">"
			+ "				<g><path fill=\"#ea8010\" d=\"M500,10c-46.7,0-84.5,38-84.5,84.9v573.7c0,46.9,37.8,84.9,84.5,84.9c46.7,0,84.5-38,84.5-84.9V94.9C584.5,"
			+ "48,546.7,10,500,10z M500,821c-46.7,0-84.5,37.8-84.5,84.5c0,46.7,37.8,84.5,84.5,84.5c46.7,0,84.5-37.8,84.5-84.5C584.4,858.9,546.6,821,500,821z\" /></g>"
			+ "			</svg></td></tr>" + "		<tr><td><br />%s</td>" + "</tr></table></div>";
}
