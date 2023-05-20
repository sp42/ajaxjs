package com.ajaxjs.workflow.model.po;

import com.ajaxjs.util.map.JsonHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 工作项（待办、已处理任务的查询结果实体）
 */
@Data
public class WorkItem implements Serializable {
    private static final long serialVersionUID = 2630386406754942892L;

    private String id;

    /**
     * 流程定义ID
     */
    private String processId;

    /**
     * 流程实例ID
     */
    private String orderId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程实例url
     */
    private String instanceUrl;

    /**
     * 流程实例为子流程时，该字段标识父流程实例ID
     */
    private String parentId;

    /**
     * 流程实例创建者ID
     */
    private String creator;

    /**
     * 流程实例创建时间
     */
    private String orderCreateTime;

    /**
     * 流程实例结束时间
     */
    private String orderEndTime;

    /**
     * 流程实例期望完成时间
     */
    private String orderExpireTime;

    /**
     * 流程实例编号
     */
    private String orderNo;

    /**
     * 流程实例附属变量
     */
    private String orderVariable;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务标识名称
     */
    private String taskKey;

    /**
     * 参与类型（0：普通任务；1：参与者fork任务[即：如果10个参与者，需要每个人都要完成，才继续流转]）
     */
    private Integer performType;
    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 任务状态（0：结束；1：活动）
     */
    private Integer taskState;

    /**
     * 任务创建时间
     */
    private String taskCreateTime;

    /**
     * 任务完成时间
     */
    private String taskEndTime;

    /**
     * 期望任务完成时间
     */
    private String taskExpireTime;

    /**
     * 任务附属变量
     */
    private String taskVariable;

    /**
     * 任务处理者ID
     */
    private String operator;

    /**
     * 任务关联的表单url
     */
    private String actionUrl;

    /**
     * 任务参与者列表
     */
    private String[] actorIds;


    public Map<String, Object> getOrderVariableMap() {
        Map<String, Object> map = JsonHelper.parseMap(this.orderVariable);
        return map == null ? Collections.emptyMap() : map;
    }

    public Map<String, Object> getTaskVariableMap() {
        Map<String, Object> map = JsonHelper.parseMap(this.taskVariable);
        return map == null ? Collections.emptyMap() : map;
    }

    public String toString() {
        return "WorkItem(processId=" + this.processId +
                ",orderId=" + this.orderId +
                ",taskId=" + this.taskId +
                ",processName" + this.processName +
                ",taskType=" + this.taskType +
                ",taskName" + this.taskName +
                ",performType=" + this.performType + ")";
    }

}
