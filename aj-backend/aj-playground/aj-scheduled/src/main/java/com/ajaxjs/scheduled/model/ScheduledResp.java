package com.ajaxjs.scheduled.model;

import lombok.Data;

/**
 *
 */
@Data
public class ScheduledResp {
    private Object data;

    private String message;

    private String code;

    public ScheduledResp(Object data, String message, String code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    /**
     * @param data
     * @return
     */
    public static ScheduledResp successInstance(Object data) {
        return new ScheduledResp(data == null ? "" : data, "", "0");
    }

    /**
     * @return
     */
    public static ScheduledResp successInstance() {
        return new ScheduledResp("", "", "0");
    }

    public static ScheduledResp failInstance() {
        return new ScheduledResp("", "", "1");
    }
}
