package com.ajaxjs.scheduled.model;

import lombok.Data;

@Data
public class JobInfoVO {
    private Long id;

    private String jobName;

    private String jobExpress;

    private String jobAppName;

    private String jobClassName;

    private String jobMethodName;

    private Integer jobStatus;
}
