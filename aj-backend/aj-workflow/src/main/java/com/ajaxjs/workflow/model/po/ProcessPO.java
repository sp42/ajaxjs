package com.ajaxjs.workflow.model.po;

import com.ajaxjs.workflow.model.ProcessModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程
 *
 * @author Frank Cheung sp42@qq.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessPO extends BasePersistantObject {
    /**
     * 版本
     */
    private Integer version;

    /**
     * 流程定义显示名称
     */
    private String displayName;

    /**
     * 流程定义模型
     */
    private ProcessModel model;

    /**
     * 流程定义类型（预留字段）
     */
    private String type;

    /**
     * 当前流程的实例 url（一般为流程第一步的 url） 该字段可以直接打开流程申请的表单
     */
    private String instanceUrl;
}
