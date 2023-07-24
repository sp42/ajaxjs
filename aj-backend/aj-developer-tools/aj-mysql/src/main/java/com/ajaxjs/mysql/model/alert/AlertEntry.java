/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model.alert;

import java.util.Date;

import com.ajaxjs.util.DateUtil;

import lombok.Data;

/**
 * Placeholder for alert information
 *
 * @author xrao
 */
@Data
public class AlertEntry implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * AlertTime
     */
    private long ts; // timestamp
    private String alertReason;// the type of alert
    private String alertValue;// the value triggered alert
    private String dbGroup;// database group
    private String dbHost;// database server host

    public AlertEntry(long ts, String alertReason, String alertValue, String dbGroup, String dbHost) {
        this.ts = ts;
        this.alertReason = alertReason;
        this.alertValue = alertValue;
        this.dbGroup = dbGroup;
        this.dbHost = dbHost;
    }

    public String getAlertTime() {
        return DateUtil.formatDate(new Date(ts));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ALERT: ").append(this.alertReason);

        if (this.alertValue != null && !this.alertValue.isEmpty())
            sb.append(", ").append(this.alertValue);

        sb.append(", ").append(this.dbGroup).append(", ").append(this.dbHost);
        sb.append(", TIME: ").append(getAlertTime());

        return sb.toString();
    }

}
