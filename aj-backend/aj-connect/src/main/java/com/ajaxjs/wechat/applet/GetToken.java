package com.ajaxjs.wechat.applet;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.logger.LogHelper;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 获取小程序 AccessToken
 */
public class GetToken {
    private static final LogHelper LOGGER = LogHelper.getLog(GetToken.class);

    private final static String TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token";

    public final WeChatAppletConfig appletCfg;

    /**
     * @param appletCfg 包含 AppId 和密钥
     */
    public GetToken(WeChatAppletConfig appletCfg) {
        this.appletCfg = appletCfg;
    }

    /**
     * 当获取到 AccessToken 的时候触发，例如放进 Redis
     */
    private Consumer<String> onTokenGet;

    public Consumer<String> getOnTokenGet() {
        return onTokenGet;
    }

    public void setOnTokenGet(Consumer<String> onTokenGet) {
        this.onTokenGet = onTokenGet;
    }

    /**
     * 获取 Client AccessToken
     */
    public void getAccessToken() {
        LOGGER.info("获取 Client AccessToken");

        String params = String.format("?grant_type=client_credential&appid=%s&secret=%s", appletCfg.getAccessKeyId(), appletCfg.getAccessSecret());
        Map<String, Object> map = Get.api(TOKEN_API + params);

        if (map.containsKey("access_token")) {
            String accessToken = map.get("access_token").toString();
            appletCfg.setAccessToken(accessToken);
            LOGGER.warning("获取令牌成功！ AccessToken [{0}]", appletCfg.getAccessToken());

            if (onTokenGet != null) {
                try {
                    onTokenGet.accept(accessToken);
                } catch (Throwable e) {
                    LOGGER.warning(e);
                }
            }
        } else if (map.containsKey("errcode"))
            LOGGER.warning("获取令牌失败！ Error [{0}:{1}]", map.get("errcode"), map.get("errmsg"));
        else
            LOGGER.warning("获取令牌失败！未知异常 [{0}]", map);
    }

    /**
     * 获取 Client AccessToken，并加入定时器
     */
    public void init() {
        getAccessToken();
        SetTimeout.simpleTimeout(this::getAccessToken, 7100);
    }
}
