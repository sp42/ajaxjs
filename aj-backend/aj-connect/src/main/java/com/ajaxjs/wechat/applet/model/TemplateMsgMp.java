package com.ajaxjs.wechat.applet.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.Map;

/**
 * 公众号模板消息相关的信息，可以参考公众号模板消息接口
 */
@Data
public class TemplateMsgMp implements IBaseModel {
    /**
     * 公众号 appid，要求与小程序有绑定且同主体
     */
    private String appid;

    /**
     * 公众号模板id
     */
    private String template_id;

    /**
     * 公众号模板消息所要跳转的url
     */
    private String url;

    /**
     * 公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系
     */
    private Miniprogram miniprogram;

    /**
     * 公众号模板消息的数据
     */
    private Map<String, Object> data;

    @Data
    public static class Miniprogram implements IBaseModel {
        private String appid;
        private String pagepath;
    }
}
