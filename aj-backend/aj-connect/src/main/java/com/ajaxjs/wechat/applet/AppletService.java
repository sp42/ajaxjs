package com.ajaxjs.wechat.applet;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.applet.model.Code2SessionResult;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 小程序常见业务
 */
public class AppletService {
    private static final LogHelper LOGGER = LogHelper.getLog(AppletService.class);

    public final WeChatAppletConfig appletCfg;

    public WeChatAppletConfig getAppletCfg() {
        return appletCfg;
    }

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
            LOGGER.warning("小程序登录成功！ OpenId [{0}]", result.getOpenid());
        } else if (result.getErrcode() != null) {
            LOGGER.warning("小程序登录失败！ Error [{0}:{1}]", result.getErrcode(), result.getErrmsg());
        } else
            LOGGER.warning("小程序登录失败，未知异常 [{0}]", result);

        return result;
    }

    // 生成小程序码的 API
    private static final String CREATE_QRCODE_API = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

    private static final String QRCODE_ARGS = "{\"scene\":\"%s\",\"page\":\"%s\",\"width\":%d,\"is_hyaline\":%s,\"env_version\":\"%s\"}";

    /**
     * 获取小程序码
     *
     * @param isTrialVersion 是否生成的是体验版的小程序码
     * @param scene          场景值，最长为 32 个字符。使用 JSON 编码，扩展性比较好
     * @param page           小程序页面路径，例如 pages/index/index
     * @param width          二维码的宽度，单位 px，最小 280px，最大 1280px
     * @param fileName       文件名
     */
    public void qrCode(boolean isTrialVersion, String scene, String page, int width, String fileName) {
        String url = CREATE_QRCODE_API + appletCfg.getAccessToken();
        // env_version 正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
        // 配置参数
        String requestJson = String.format(QRCODE_ARGS, scene, page, width, false, isTrialVersion ? "trial" : "release");
        System.out.println(requestJson);

        // 请求
        Post.showPic(url, conn -> {
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(requestJson.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, DiContextUtil.getResponse());
    }

    public static String mapToQueryString(Map<String, Object> map) {
        StringBuilder queryString = new StringBuilder();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            if (queryString.length() > 0)
                queryString.append('&');

            queryString.append(key).append('=').append(value);
        }

        return queryString.toString();
    }
}
