package com.ajaxjs.wechat.applet.payment;

import com.ajaxjs.util.binrary.BytesUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.common.AesUtil;
import com.ajaxjs.wechat.merchant.MerchantConfig;

import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * 通用逻辑
 */
public abstract class CommonService {
    private static final LogHelper LOGGER = LogHelper.getLog(CommonService.class);

    abstract public MerchantConfig getMchCfg();

    /**
     * 解密回调数据
     *
     * @param params 回调数据
     * @return 解密后的 JSON 字符串
     */
    public String decrypt(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        Map<String, Object> resource = (Map<String, Object>) params.get("resource");
        LOGGER.info(params.get("summary") + String.valueOf(resource));

        // 对 resource 对象进行解密
        String ciphertext = resource.get("ciphertext").toString();
        LOGGER.info(ciphertext);

        byte[] apiV3KeyByte = BytesUtil.getUTF8_Bytes(getMchCfg().getApiV3Key());
        byte[] associatedData = BytesUtil.getUTF8_Bytes(resource.get("associated_data").toString());
        byte[] nonce = BytesUtil.getUTF8_Bytes(resource.get("nonce").toString());

        // 解密
        AesUtil aesUtil = new AesUtil(apiV3KeyByte);
        String cert = null;

        try {
            cert = aesUtil.decryptToString(associatedData, nonce, ciphertext);
        } catch (GeneralSecurityException e) {
            LOGGER.warning(e);
        }

        LOGGER.info(cert);

        return cert;
    }
}
