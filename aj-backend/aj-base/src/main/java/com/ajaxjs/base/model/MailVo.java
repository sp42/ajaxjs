package com.ajaxjs.base.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.Map;

@Data
public class MailVo implements IBaseModel {
    /**
     * 发件人邮箱
     */
    private String from;

    /**
     * 收件人邮箱
     */
    private String to;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件内容是否为 HTML 格式
     */
    private boolean isHtmlBody;

    /**
     * 附件列表，key 是文件名，byte[] 是文件内容
     */
    private Map<String, byte[]> attachment;
}
