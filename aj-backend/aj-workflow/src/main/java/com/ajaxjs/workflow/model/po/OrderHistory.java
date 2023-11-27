package com.ajaxjs.workflow.model.po;

import java.util.Date;

import com.ajaxjs.data.jdbc_helper.common.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * 历史流程实例实体类
 * <p>
 * history 减少了 version，增加了 endDate 字段
 */
@Data
@TableName("wf_order_history")
@EqualsAndHashCode(callSuper = true)
public class OrderHistory extends Order {
    /**
     * 创建历史流程实例
     */
    public OrderHistory() {
    }

    /**
     * 根据 Order 创建历史流程实例
     *
     * @param order 活动实例对象
     */
    public OrderHistory(Order order) {
        BeanUtils.copyProperties(order, this);
        setVersion(null); // 排除 version，历史表里面没有该字段
    }

    /**
     * 根据历史实例撤回活动实例
     *
     * @return 活动实例对象
     */
    public Order undo() {
        Order order = new Order();
        order.setId(getId());
        order.setProcessId(getProcessId());
        order.setParentId(getParentId());
        order.setCreator(getCreator());
        order.setCreateDate(getCreateDate());
        order.setUpdater(getUpdater());
        order.setUpdateDate(getEndDate());
        order.setExpireDate(getExpireDate());
        order.setOrderNo(getOrderNo());
        order.setPriority(getPriority());
        order.setVariable(getVariable());
        order.setVersion(0);

        return order;
    }

    /**
     * 流程实例结束时间
     */
    private Date endDate;

}
