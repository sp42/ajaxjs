package com.ajaxjs.scheduled.model;

import lombok.Data;

/**
 * 参数
 */
@Data
public class ScheduledParam {
    private Long id;
    private String jobClassName;
    private String jobExpress;
    private String jobMethodName;
    private String jobName;
    private int pageNo;
    private int pageSize;
}
