package com.ajaxjs.web.secuity;

import java.util.List;
import java.util.regex.Pattern;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.web.mvc.MvcRequest;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

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
			throw new SecurityException(String.format("地址[%s]已列入黑名单！", str));

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
	public boolean before(FilterContext ctx) {
		refererCheck(ctx.request);

		if (!"GET".equalsIgnoreCase(ctx.request.getMethod()))
			return true;

		String uri = ctx.request.getRequestURI();

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

			if (!referer.startsWith(request.getServerName()))
				throw new SecurityException("Referer:" + referer + " 来路检测不通过");
		}
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
