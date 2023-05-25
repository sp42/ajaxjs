package com.ajaxjs.wechat.merchant;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.common.PemUtil;
import com.ajaxjs.wechat.common.RsaCryptoUtil;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * 签名生成器
 *
 * @author Frank Cheung
 */
public class SignerMaker {
    private static final LogHelper LOGGER = LogHelper.getLog(SignerMaker.class);

    private final MerchantConfig cfg;

    protected final PrivateKey privateKey;

    /**
     * 创建签名生成器
     *
     * @param cfg 商户平台的配置
     */
    public SignerMaker(MerchantConfig cfg) {
        this.cfg = cfg;
//        this.privateKey = PemUtil.loadPrivateKey(cfg.getPrivateKey());
        this.privateKey = PemUtil.loadPrivateKeyByPath(cfg.getPrivateKey());
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
     * 生成签名明文
     *
     * @param request   请求信息
     * @param nonceStr  随机字符串
     * @param timestamp 时间戳
     * @return 签名明文
     */
    private static String buildMessage(HttpRequestWrapper request, String nonceStr, long timestamp) {
        // @formatter:off
        return request.method + "\n"
            + request.url + "\n"
            + timestamp + "\n"
            + nonceStr + "\n"
            + request.body + "\n";
        // @formatter:on
    }
}
