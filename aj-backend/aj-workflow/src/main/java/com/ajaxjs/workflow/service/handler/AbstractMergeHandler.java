package com.ajaxjs.workflow.service.handler;

import java.util.List;

import com.ajaxjs.workflow.common.WfData;
import org.springframework.util.CollectionUtils;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.TaskService;

/**
 * 合并处理的抽象处理器 需要子类提供查询无法合并的 task 集合的参数 map
 */
public abstract class AbstractMergeHandler implements IHandler {
    /**
     * 处理当前流程实例的合并状态。此方法查询当前流程中无法参与合并的节点列表，并根据子流程和任务的完成状态，
     * 更新流程实例的合并状态。如果所有中间节点都已完成，流程将被设置为已合并状态，允许模型继续执行 join 的输出变迁。
     *
     * @param exec 表示当前执行流程实例的对象。
     */
    @Override
    public void handle(Execution exec) {
        Long orderId = exec.getOrder().getId(); // 获取当前订单ID和流程模型
        ProcessModel model = exec.getModel();
        String[] activeNodes = findActiveNodes(); // 查询当前活跃的节点

        // 初始化子流程和任务的合并状态
        boolean isSubProcessMerged = false, isTaskMerged = false;

        // 检查子流程是否可以被合并
        if (model.containsNodeNames(SubProcessModel.class, activeNodes)) {
            List<Order> orders = WfData.findByIdAndExcludedIds(orderId, exec.getChildOrderId());// 根据订单ID和排除的子订单ID查询未完成的子订单

            if (CollectionUtils.isEmpty(orders))    // 如果没有未完成的子订单，则表示子流程可以被合并
                isSubProcessMerged = true;
        } else {
            // 如果当前流程实例不包含子流程，则直接设置子流程为可合并状态
            isSubProcessMerged = true;
        }

        // 获取任务服务
        TaskService taskService = exec.getEngine().taskService;

        // 检查任务是否可以被合并
        if (isSubProcessMerged && model.containsNodeNames(TaskModel.class, activeNodes)) {
            // 根据订单ID、排除当前任务ID和活跃节点ID查询未完成的任务
            List<Task> tasks = WfData.findByOrderIdAndExcludedIds(orderId, exec.getTask().getId(), activeNodes);

            // 如果没有未完成的任务，则表示任务可以被合并
            if (CollectionUtils.isEmpty(tasks))
                isTaskMerged = true;
        }

        // 根据子流程和任务的合并状态，更新流程实例的合并状态
        exec.setMerged(isSubProcessMerged && isTaskMerged);
    }

    /**
     * 子类需要提供如何查询未合并任务的参数 map
     *
     * @return 当前流程实例的无法参与合并的 node 列表
     */
    protected abstract String[] findActiveNodes();
}
