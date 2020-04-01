/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework.filter;

import java.lang.reflect.Method;
import java.util.Date;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.cryptography.AES_Cipher;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 简单的接口合法性校验，基于 AES
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class AesFilter implements FilterAction {
	private static final LogHelper LOGGER = LogHelper.getLog(AesFilter.class);

	public static final String requestQueryStringParamterName = "token";

	/**
	 * 默认的 key 当配置没有 key 的情况读取这个 默认的 key
	 */
	public static String aesKey = "hihi";

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		String errMsg = null;
		String p = request.getParameter(requestQueryStringParamterName);

		if (CommonUtil.isEmptyString(p)) {
			errMsg = "缺少参数 " + requestQueryStringParamterName;
			LOGGER.info(errMsg);
			request.setAttribute("errMsg", errMsg);
			return false;
		}

		Date requestDate = decryptTimeStampToken(p, aesKey);

		if (requestDate == null)
			errMsg = "解密失败！";
		else {
			long requesDuration = new Date().getTime() - requestDate.getTime();

			if (requesDuration < d)
				return true;
			else
				errMsg = "请求超时！";
		}

		if (errMsg != null)
			request.setAttribute("errMsg", errMsg);

		return false;
	}

	final int d = 15 * 60000; // 15分钟

	@Override
	public boolean after(FilterAfterArgs args) {
		if (args.request.getAttribute("errMsg") != null) {
			args.response.resultHandler("redirect::", args.request, null, args.method);

			return false;
		}

		return true;
	}

	/**
	 * 创建时间戳 Token
	 * 
	 * @param aesKey AES 密钥
	 * @return 时间戳 Token
	 */
	public static String getTimeStampToken(String aesKey) {
		return AES_Cipher.AES_Encrypt(System.currentTimeMillis() + "", aesKey);
	}

	/**
	 * 从加密的 Token 中还原时间戳
	 * 
	 * @param token  时间戳 Token
	 * @param aesKey AES 密钥
	 * @return 返回 null 表示解密失败！
	 */
	public static Date decryptTimeStampToken(String token, String aesKey) {
		String timeStamp = AES_Cipher.AES_Decrypt(token + "", aesKey);
		if (timeStamp == null)
			return null;

		long t = Long.parseLong(timeStamp);
		return new Date(t);
	}
}
