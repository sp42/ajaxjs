package com.ajaxjs.wechat.applet.model;


import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 小程序模板消息相关的信息，可以参考小程序模板消息接口
 */
@Data
public class TemplateMsgWebApp implements IBaseModel {
    /**
     * 小程序模板 ID
     */
    private String template_id;

    /**
     * 小程序页面路径
     */
    private String page;

    /**
     * 小程序模板消息 formId
     */
    private String form_id;

    /**
     * 小程序模板放大关键词
     */
    private String emphasis_keyword;

    /**
     * 小程序模板数据
     */
    private String data;
}
