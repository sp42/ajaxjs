package com.ajaxjs.workflow.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务参与者实体类
 */
@Data
public class TaskActor implements Serializable {
    private static final long serialVersionUID = 2969915022122094614L;

    /**
     * 关联的任务 id
     */
    private Long taskId;

    /**
     * 关联的参与者 id（参与者可以为用户、部门、角色）
     */
    private Long actorId;
}
