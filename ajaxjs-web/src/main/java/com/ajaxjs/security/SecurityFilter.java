package com.ajaxjs.security;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.util.CommonUtil;

/**
 * HTTP 中的 POST、PUT、DELETE 都是写入的方法，这里对其检测
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SecurityFilter implements FilterAction {
	/**
	 * 白名单
	 */
	public List<String> whiteList;

	/**
	 * 黑名单
	 */
	public List<String> blackList;

	/**
	 * 是否在白名单列表中
	 * 
	 * @param str 待检查的字符串
	 * @return true 表示为包含在白名单；false 表示为不包含在白名单
	 */
	public boolean isInWhiteList(String str) {
		return isInList(str, whiteList);
	}

	/**
	 * 是否在黑名单列表中。黑名单会专门抛出异常，以便记录。
	 * 
	 * @param str 待检查的字符串
	 * @return true 表示为包含在黑名单；false 表示为不包含在黑名单
	 */
	public boolean isInBlackList(String str) {
		boolean isIn = isInList(str, blackList);

		if (!isIn)
			throw new SecurityException(String.format("地址 %s 已列入黑名单！", str));

		return isIn;
	}

	/**
	 * 是否在列表中
	 * 
	 * @param str
	 * @param list
	 * @return true 表示为包含；false 表示为不包含
	 */
	private static boolean isInList(String str, List<String> list) {
		if (CommonUtil.isNull(list))
			return false;

		for (String pattern : list) {
			if (Pattern.matches(pattern, str))
				return true;
		}

		return false;
	}

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		refererCheck(request);

		if (!"GET".equalsIgnoreCase(request.getMethod()))
			return true;

		String uri = request.getRequestURI();

		if (isInWhiteList(uri))
			return true;
		if (isInBlackList(uri))
			return false;

		return true; // 没有任何信息则通过
	}

	/**
	 * 来路检测
	 * 
	 * @param request 请求对象
	 */
	private static void refererCheck(MvcRequest request) {
		if (ConfigService.getValueAsBool("security.isRefererCheck")) {
			String referer = request.getHeader("referer");

			if (CommonUtil.isEmptyString(referer))
				throw new SecurityException("请求没有 referer 字段不通过");

			if (!referer.startsWith(request.getServerName())) {
				throw new SecurityException("Referer:" + referer + " 来路检测不通过");
			}
		}
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
