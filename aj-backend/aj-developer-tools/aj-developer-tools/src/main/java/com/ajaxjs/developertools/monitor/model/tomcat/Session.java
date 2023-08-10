package com.ajaxjs.developertools.monitor.model.tomcat;

import com.ajaxjs.framework.IBaseModel;

public class Session implements IBaseModel {
    /**
     * 活动会话数
     */
    public int sessionCounter;

    /**
     * 会话数
     */
    public int activeSessions;

    /**
     * 最大会话数
     */
    public int maxActiveSessions;
}
