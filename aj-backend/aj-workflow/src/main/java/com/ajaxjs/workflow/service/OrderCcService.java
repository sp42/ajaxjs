package com.ajaxjs.workflow.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.workflow.common.WfConstant;
import com.ajaxjs.workflow.model.po.OrderCc;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 委托
 *
 * @author Frank Cheung
 */
@Component
public class OrderCcService {
    private final String sql = "SELECT * FROM wf_order_cc WHERE order_id = ?";

    /**
     * 根据实例 id 查找抄送列表
     *
     * @param orderId 流程实例 id
     * @return 抄送列表
     */
    public List<OrderCc> findByOrderId(Long orderId) {
        return CRUD.list(OrderCc.class, sql, orderId);
    }

    /**
     * 根据实例 id 和 参与者列表 查找抄送列表
     *
     * @param orderId  流程实例 id
     * @param actorIds 参与者 id 列表
     * @return 抄送列表
     */
    public List<OrderCc> findList(Long orderId, Long... actorIds) {
        String _sql = sql + " AND actor_id IN (" + StrUtil.join(actorIds, ",") + ")";

        return CRUD.list(OrderCc.class, _sql, orderId);
    }

    /**
     * 创建抄送实例
     *
     * @param orderId  流程实例 id
     * @param creator  创建人 id
     * @param actorIds 参与者 id 列表
     */
    public void createCCOrder(Long orderId, Long creator, Long... actorIds) {
        for (Long actorId : actorIds) {
            OrderCc coder = new OrderCc();
            coder.setOrderId(orderId);
            coder.setCreator(creator);
            coder.setActorId(actorId);
            coder.setStat(WfConstant.STATE_ACTIVE);

            CRUD.create(coder);
        }
    }

    /**
     * 更新抄送记录为已阅
     *
     * @param orderId  流程实例 id
     * @param actorIds 参与者 id 列表
     */
    public void updateCCStatus(Long orderId, Long... actorIds) {
        List<OrderCc> orders = findList(orderId, actorIds);

        for (OrderCc order : orders) {
            order.setStat(WfConstant.STATE_FINISH);
            order.setFinishDate(new Date());
            CRUD.update(order);
        }
    }

    /**
     * 删除指定的抄送记录
     *
     * @param orderId 流程实例 id
     * @param actorId 参与者 id
     */
    public void deleteCCOrder(Long orderId, Long actorId) {
        List<OrderCc> orders = findList(orderId, actorId);

        for (OrderCc order : orders)
            CRUD.delete(order, order.getId());
    }
}
