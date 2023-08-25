package com.ajaxjs.framework.spring.scheduled;

import com.ajaxjs.data.jdbc_helper.common.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("schedule_job")
public class JobInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务表达式
     */
    private String express;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 任务执行类
     */
    private String className;

    /**
     * 任务分组
     */
    private String method;

    /**
     * 任务状态：0 进行中 1 任务取消 2 任务删除
     */
    private Integer status;

    private Date createDate;

    private Date updateDate;

    /**
     * 任务状态
     */
    public interface ScheduledConstant {

        /**
         * 任务删除
         */
        Integer DELETE_STATUS = 2;

        /**
         * 任务取消
         */
        Integer CANCEL_STATUS = 1;

        /**
         * 进行中
         */
        Integer NORMAL_STATUS = 0;
    }
}
