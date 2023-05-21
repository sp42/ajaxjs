package com.ajaxjs.workflow.common;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.OrderHistory;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.model.po.TaskHistory;

import java.util.List;
import java.util.Objects;

/**
 * DAO
 */
public interface WfData {
    /**
     * 根据 id 获取任务
     *
     * @param id 任务 id
     * @return 任务
     */
    static Task findTask(Long id) {
        Task task = CRUD.info(Task.class, "SELECT * FROM wf_task WHERE id = ?", id);
        Objects.requireNonNull(task, "指定的任务[id=" + id + "]不存在");

        return task;
    }

    /**
     * 根据流程 id 查找所有的任务
     *
     * @param orderId 流程 id
     * @return 所有的任务
     */
    static List<Task> findTasksByOrderId(Long orderId) {
        return CRUD.list(Task.class, "SELECT * FROM wf_task WHERE order_id = ?", orderId);
    }

    static List<Task> findTasksByParentTaskId(Long parentTaskId) {
        return CRUD.list(Task.class, "SELECT * FROM wf_task WHERE parent_task_id = ?", parentTaskId);
    }

    static TaskHistory findTaskHistory(Long id) {
        return CRUD.info(TaskHistory.class, "SELECT * FROM wf_task_history WHERE id = ?", id);
    }

    /**
     * 根据流程 id 查找所有的历史任务
     *
     * @param orderId 流程 id
     * @return 所有的历史任务
     */
    static List<TaskHistory> findHistoryTasksByOrderId(Long orderId) {
        return CRUD.list(TaskHistory.class, "SELECT * FROM wf_task_history WHERE order_id = ?", orderId);
    }

    /**
     * 根据流程 id 和任务名称查找所有的历史任务
     *
     * @param orderId  流程 id
     * @param taskName 任务名称
     * @return 所有的历史任务
     */
    static List<TaskHistory> findHistoryTasksByOrderIdAndTaskName(Long orderId, String taskName) {
        return CRUD.list(TaskHistory.class, "SELECT * FROM wf_task_history WHERE order_id = ? AND name = ?", orderId, taskName);
    }

    static void createTaskHistory(TaskHistory history) {
        CRUD.create(history);
    }

    static Order findOrder(Long id) {
        return CRUD.info(Order.class, "SELECT * FROM wf_order WHERE id = ?", id);
    }

    static List<Order> findByIdAndExcludedIds(Long parentId, Long... childOrderId) {
        String sql = "SELECT * FROM wf_order WHERE parent_id = ? AND id NOT IN (" + StrUtil.join(childOrderId, ",") + ")";

        return CRUD.list(Order.class, sql, parentId);
    }

    static OrderHistory findOrderHistory(Long id) {
        return CRUD.info(OrderHistory.class, "SELECT * FROM wf_order_history WHERE id = ?", id);
    }


}
