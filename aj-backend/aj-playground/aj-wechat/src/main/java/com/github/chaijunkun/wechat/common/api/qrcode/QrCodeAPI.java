package com.github.chaijunkun.wechat.common.api.qrcode;

import com.github.chaijunkun.wechat.common.api.WeChatAPI;
import com.github.chaijunkun.wechat.common.api.WeChatAPIException;

/**
 * 获取二维码接口
 */
public class QrCodeAPI extends WeChatAPI<QrCodeURLFactory> {

    @Override
    public QrCodeURLFactory getUrlFactory() {
        return urlFactory;
    }

    @Override
    public void setUrlFactory(QrCodeURLFactory urlFactory) {
        this.urlFactory = urlFactory;
    }

    public QrCodeResult getQrCode(QrCodeParam param) throws WeChatAPIException {
        return doPostAPIWithAccessToken(urlFactory.getQrCodeUrl(), param, QrCodeResult.class);
    }

}
