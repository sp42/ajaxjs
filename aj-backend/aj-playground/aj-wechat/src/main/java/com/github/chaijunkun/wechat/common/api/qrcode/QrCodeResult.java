package com.github.chaijunkun.wechat.common.api.qrcode;

import com.github.chaijunkun.wechat.common.api.WeChatAPIRet;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 二维码响应代码
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QrCodeResult extends WeChatAPIRet {
    private static final long serialVersionUID = 4386631314677341070L;

    private String ticket;

    private int expire_seconds;

    private String url;
}
