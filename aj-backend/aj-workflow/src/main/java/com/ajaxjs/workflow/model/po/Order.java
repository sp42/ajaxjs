package com.ajaxjs.workflow.model.po;

import com.ajaxjs.framework.entity.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 流程工作单实体类（一般称为流程实例）
 */

@Data
@TableName("wf_order")
@EqualsAndHashCode(callSuper = true)
public class Order extends BasePersistantObject {
    /**
     * 版本
     */
    private Integer version;

    /**
     * 流程定义 id
     */
    private Long processId;

    /**
     * 流程实例为子流程时，该字段标识父流程实例 id
     */
    private Long parentId;

    /**
     * 流程实例为子流程时，该字段标识父流程哪个节点模型启动的子流程
     */
    private String parentNodeName;

    /**
     * 流程实例期望完成时间
     */
    private Date expireDate;

    /**
     * 流程实例优先级
     */
    private Integer priority;

    /**
     * 流程实例编号
     */
    private String orderNo;

    /**
     * 流程实例附属变量
     */
    private String variable;

    public String toString() {
        return "Order(id=" + getId() +
                ",processId=" + processId +
                ",creator=" + getCreator() +
                ",createDate" + getCreateDate() +
                ",orderNo=" + orderNo + ")";
    }
}
