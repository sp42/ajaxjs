/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.base.service.message.email;

import lombok.Data;

import java.util.Map;

/**
 * 邮件模型
 */
@Data
public class Mail {
    /**
     * 邮件服务器地址
     */
    private String mailServer;

    /**
     * SMTP 服务器的端口
     */
    private int port = 25;

    /**
     * 发件人账号
     */
    private String account;

    /**
     * 发件人密码
     */
    private String password;

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

    public String getMailServer() {
        if (mailServer == null)
            throw new IllegalArgumentException("没有指定 MailServer！");

        return mailServer;
    }

    public String getFrom() {
        if (from == null)
            throw new IllegalArgumentException("没有指定发件人！");

        return from;
    }

    public String getTo() {
        if (to == null)
            throw new IllegalArgumentException("没有指定收件人！");

        return to;
    }

    public boolean isHtmlBody() {
        return isHtmlBody;
    }

    public void setHtmlBody(boolean isHTML_body) {
        this.isHtmlBody = isHTML_body;
    }

}
