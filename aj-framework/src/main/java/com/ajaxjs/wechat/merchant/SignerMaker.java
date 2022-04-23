package com.ajaxjs.wechat.merchant;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.applet.util.PemUtil;
import com.ajaxjs.wechat.applet.util.RsaCryptoUtil;

/**
 * 签名生成器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class SignerMaker {
	private static final LogHelper LOGGER = LogHelper.getLog(SignerMaker.class);

	private MerchantConfig cfg;

	protected final PrivateKey privateKey;

	/**
	 * 创建签名生成器
	 * 
	 * @param cfg 商户平台的配置
	 */
	public SignerMaker(MerchantConfig cfg) {
		this.cfg = cfg;
		this.privateKey = PemUtil.loadPrivateKey(cfg.getPrivateKey());
	}

	/**
	 * 生成签名
	 * 
	 * @param request 请求信息
	 * @return 签名 Token
	 */
	public String getToken(HttpRequestWrapper request) {
		String nonceStr = StrUtil.getRandomString(32);
		long timestamp = System.currentTimeMillis() / 1000;

		String message = buildMessage(request, nonceStr, timestamp);
//		LOGGER.debug("authorization message=[{0}]", message);

		String signature = RsaCryptoUtil.sign(privateKey, message.getBytes(StandardCharsets.UTF_8));

		// @formatter:off
        String token = "mchid=\"" + cfg.getMchId() + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + cfg.getMchSerialNo() + "\","
                + "signature=\"" + signature + "\"";
        // @formatter:on

		LOGGER.debug("authorization token=[{0}]", token);

		return token;
	}

	/**
	 * 
	 * @param request   请求信息
	 * @param nonceStr
	 * @param timestamp
	 * @return
	 */
	static String buildMessage(HttpRequestWrapper request, String nonceStr, long timestamp) {
		// @formatter:off
	    return request.method + "\n"
	        + request.url + "\n"
	        + timestamp + "\n"
	        + nonceStr + "\n"
	        + request.body + "\n";
	    // @formatter:on
	}
}
