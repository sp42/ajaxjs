package com.ajaxjs.user.filter;

import java.lang.reflect.Method;
import java.util.Objects;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.user.token.TokenInfo;
import com.ajaxjs.user.token.TokenService;

/**
 * 简单的接口合法性校验，基于 AES
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ApiAllowRequestCheck implements FilterAction {
	/**
	 * 凭证验证服务
	 */
	private final static TokenService service = new TokenService(new TokenInfo());

	static {
		service.getInfo().setUsingTimeout(true);
		service.getInfo().setKeyByConfig("System.api.AES_Key");
		service.getInfo().setTimeoutByConfig("System.api.timeout");
	}

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		String token = request.getHeader(TokenInfo.TOKEN);
		if (token == null)
			token = request.getParameter(TokenInfo.TOKEN);
		Objects.requireNonNull(token, "缺少 token 参数，请放置 HTTP Header 请求中或 QueryString 中");

		return service.verify(token);
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;

	}

	/**
	 * 创建时间戳 Token
	 * 
	 * @return 时间戳 Token
	 */
	public static String getTimeStampToken() {
		return service.getToken();
	}
}
