package com.ajaxjs.user.filter;

import java.lang.reflect.Method;
import java.util.Date;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.util.cryptography.SymmetricCipher;

public class ApiAllowRequestCheck implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method, Object[] args) {
		String token = request.getHeader("token");

		String AES_Key = ConfigService.getValueAsString("Symmetric.AES_Key");
		if (token == null)
			throw new NullPointerException("缺少 token 参数，请放置 HTTP Header 请求中");

		if (AES_Key == null)
			throw new NullPointerException("缺少 Symmetric.AES_Key 配置");

		String time = SymmetricCipher.AES_Decrypt(token, AES_Key);

		if (time == null) {
			throw new IllegalAccessError("合法性请求解密的密码不正确");
		}

		time = time.replaceAll("token_", "");

		long datestamp = 0L;

		try {
			datestamp = Long.parseLong(time);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("转换时间戳格式不正确！");
		}

		long diff = new Date().getTime() - datestamp;

		if (diff > ConfigService.getValueAsLong("Symmetric.apiTimeout")) {
			throw new IllegalAccessError("请求超时！");
		} else {
			return true;
		}
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {

	}

}
