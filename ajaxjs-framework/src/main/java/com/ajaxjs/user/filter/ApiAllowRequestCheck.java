package com.ajaxjs.user.filter;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.user.service.TokenMaker;
import com.ajaxjs.util.cryptography.Symmetri_Cipher;

/**
 * 简单的接口合法性校验，基于 AES
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ApiAllowRequestCheck implements FilterAction {
	/**
	 * 参数名称
	 */
	public static final String TOKEN = "token";

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		String token = request.getHeader(TOKEN);
		if (token == null)
			token = request.getParameter(TOKEN);
		Objects.requireNonNull(token, "缺少 token 参数，请放置 HTTP Header 请求中或 QueryString 中");

		String decrypted = Symmetri_Cipher.AES_Decrypt(token, ConfigService.getValueAsString("System.api.AES_Key"));

		return TokenMaker.checkTimespam(ConfigService.getValueAsInt("System.api.timeout"), null).test(decrypted);
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
		Function<String, String> fn = TokenMaker::addTimespam;
		fn = fn.andThen(TokenMaker.encryptAES(ConfigService.getValueAsString("System.api.AES_Key")));
		return fn.apply("{timeStamp}");
	}
}
