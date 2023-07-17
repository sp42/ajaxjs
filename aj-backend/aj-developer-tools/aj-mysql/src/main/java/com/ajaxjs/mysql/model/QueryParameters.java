/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * Wrap query parameters
 */
@Data
public class QueryParameters implements Serializable {
    private static final long serialVersionUID = 6906313101027653783L;
    private String group;// an identifier for the target database
    private String host;// DB hostname
    private String port;// DB port
    private String svc;// servicename
    private String sid;// sid
    private String sql;// an internal sql id
    private int targetInstance = 0;// 0 is invalid number, meaning no target instance is passed. If it is passed,
    // it will be in higher priority than the one derived from host

    private Map<String, String> sqlParams = new java.util.TreeMap<>();
    // parameter in p_1, p_2,..., name convention

    private int maxRows = 5000;// maximum rows to return

    private String sqlText;// So we can use it for non predefined queries.

    public QueryParameters() {
    }

    public void setGroup(String group) {
        if (group != null && group.trim().length() > 0)
            this.group = group.trim();
        else
            this.group = null;
    }

    public void setHost(String host) {
        if (host != null && host.trim().length() > 0)
            this.host = host.trim();
        else
            this.host = null;
    }

    public void setPort(String port) {
        if (port != null && port.trim().length() > 0)
            this.port = port.trim();
        else
            this.port = "1521";
    }

    public void setSvc(String svc) {
        if (svc != null && svc.trim().length() > 0)
            this.svc = svc.trim();
        else
            this.svc = null;
    }

    public void setSid(String sid) {
        if (sid != null && sid.trim().length() > 0)
            this.sid = sid.trim();
        else
            this.sid = null;
    }

    public void setSql(String sql) {
        if (sql != null && sql.trim().length() > 0)
            this.sql = sql.trim();
        else
            this.sql = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[id=").append(group).append("]");
        sb.append(",[host=").append(host).append("]");
        sb.append(",[port=").append(port).append("]");
        sb.append(",[svc=").append(svc).append("]");
        sb.append(",[sid=").append(sid).append("]");
        sb.append(",[sql=").append(sql).append("]");

        if (this.sqlParams.size() > 0) {
            for (String key : this.sqlParams.keySet()) {
                sb.append(",[").append(key).append("=").append(this.sqlParams.get(key)).append("]");
            }
        }

        return sb.toString();
    }

    /**
     * Validate the parameters. Return null or empty if validated. Otherwise, error
     * messages TODO
     */
    public String validate() {
        StringBuilder sb = new StringBuilder();
        if (group == null)
            sb.append("group is required. ");
        if (host == null)
            sb.append("host is required. ");
        // if(svc==null&&sid==null)sb.append("one of svc and sid is required. ");
        if (sql == null)
            sb.append("sql is required. ");

        return sb.toString();
    }
}
