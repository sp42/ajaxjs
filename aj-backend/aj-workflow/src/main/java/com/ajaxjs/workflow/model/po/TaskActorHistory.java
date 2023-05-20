package com.ajaxjs.workflow.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 历史任务参与者实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskActorHistory extends TaskActor {
    private static final long serialVersionUID = -998098931519373599L;
}
