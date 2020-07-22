package com.ajaxjs.user.filter;

import java.util.Objects;
import java.util.function.Function;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.user.service.TokenMaker;
import com.ajaxjs.util.cryptography.SymmetriCipher;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

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
	public boolean before(FilterContext ctx) {
		String token = ctx.request.getHeader(TOKEN);
		if (token == null)
			token = ctx.request.getParameter(TOKEN);
		Objects.requireNonNull(token, "缺少 token 参数，请放置 HTTP Header 请求中或 QueryString 中");

		String decrypted = SymmetriCipher.AES_Decrypt(token, ConfigService.getValueAsString("System.api.AES_Key"));

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
