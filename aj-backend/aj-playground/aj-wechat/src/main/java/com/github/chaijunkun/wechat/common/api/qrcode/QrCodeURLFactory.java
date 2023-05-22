package com.github.chaijunkun.wechat.common.api.qrcode;

import com.github.chaijunkun.wechat.common.api.AbstractURLFactory;
import com.github.chaijunkun.wechat.common.api.URLBean;

/**
 * 生成二维码URL
 */
public class QrCodeURLFactory extends AbstractURLFactory {

    /**
     * 生成二维码接口URL
     */
    private static final URLBean createQrCodeUrl = new URLBean(true, COMMON_API_DOMAIN, "/cgi-bin/qrcode/create");

    /**
     * 获取生成二维码接口URL
     *
     * @return 生成二维码接口URL
     */
    public String getQrCodeUrl() {
        return createQrCodeUrl.getAbsoluteURL();
    }

}
