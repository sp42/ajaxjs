
package com.ajaxjs.workflow.service.scheduling;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.ajaxjs.workflow.model.po.Task;
import lombok.Data;

/**
 * Job 实体，用于传递给具体的调度框架
 */

@Data
public class JobEntity implements Serializable {
    private static final long serialVersionUID = 5807718947643229134L;

    /**
     * 参与类型
     */
    public enum JobType {
        EXECUTOR, REMINDER
    }

    /**
     * job主键
     */
    private String key;

    /**
     * job组
     */
    private String group;

    /**
     * 任务对应的业务 id 串
     */
    private String id;

    /**
     * 节点模型名称
     */
    private String modelName;

    /**
     * job 类型
     */
    private int jobType;

    /**
     * 任务对象
     */
    private Task task;

    /**
     * 启动时间
     */
    private Date startTime;

    /**
     * 间隔时间(分钟)
     */
    private int period;

    /**
     * 执行参数
     */
    private Map<String, Object> args;

    public JobEntity(String id, Task task, Date startTime) {
        this(id, task, startTime, 0);
    }

    public JobEntity(String id, Task task, Date startTime, int period) {
        this.id = id;
        this.task = task;
        this.startTime = startTime;
        this.period = period;
    }

    public JobEntity(String id, Task task, Date startTime, Map<String, Object> args) {
        this(id, task, startTime, args, 0);
    }

    public JobEntity(String id, Task task, Date startTime, Map<String, Object> args, int period) {
        this.id = id;
        this.task = task;
        this.startTime = startTime;
        this.period = period;
        this.args = args;
    }
}
