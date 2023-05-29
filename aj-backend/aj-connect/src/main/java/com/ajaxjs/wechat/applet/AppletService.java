package com.ajaxjs.wechat.applet;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.applet.model.Code2SessionResult;

/**
 * 小程序常见业务
 */
public class AppletService {
    private static final LogHelper LOGGER = LogHelper.getLog(AppletService.class);

    public final WeChatAppletConfig appletCfg;

    /**
     * @param appletCfg 包含 AppId 和密钥
     */
    public AppletService(WeChatAppletConfig appletCfg) {
        this.appletCfg = appletCfg;
    }

    private final static String LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid=%s&secret=%s&js_code=%s";

    /**
     * 小程序登录
     *
     * @param code 登录时获取的 code，可通过wx.login获取
     * @return Code2SessionResult
     */
    public Code2SessionResult code2Session(String code) {
        LOGGER.info("小程序登录");

        String url = String.format(LOGIN_API, appletCfg.getAccessKeyId(), appletCfg.getAccessSecret(), code);
        Code2SessionResult result = Get.api2bean(url, Code2SessionResult.class);

        if (result.getOpenid() != null) {
            LOGGER.warning("小程序登录成功！ AccessToken [{0}]", result.getOpenid());
        } else if (result.getErrcode() != null) {
            LOGGER.warning("小程序登录失败！ Error [{0}:{1}]", result.getErrcode(), result.getErrmsg());
        } else
            LOGGER.warning("小程序登录失败，未知异常 [{0}]", result);

        return result;
    }

    /**
     * 获取小程序码
     */
    public class getWxCode {
    }
}
