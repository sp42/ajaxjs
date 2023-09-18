package com.ajaxjs.workflow.common;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.model.po.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * DAO
 */
public interface WfData {
    LogHelper LOGGER = LogHelper.getLog(WfData.class);

    static List<ProcessPO> findProcess() {
        return CRUD.list(ProcessPO.class, "SELECT * FROM wf_process");
    }

    static ProcessPO findProcess(Long id) {
        return CRUD.info(ProcessPO.class, "SELECT * FROM wf_process WHERE id = ?", id);
    }

    static List<ProcessPO> findProcess(String name, Integer version) {
        String sql = "SELECT * FROM wf_process WHERE name = ?";

        if (version != null)
            sql += " AND version = " + version;

        return CRUD.list(ProcessPO.class, sql, name);
    }

    static Integer getLatestProcessVersion(String name) {
        return CRUD.jdbcReaderFactory().queryOne("SELECT max(version) FROM wf_process WHERE name = ?", Integer.class, name);
    }

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

    static List<Task> findByOrderIdAndExcludedIds(Long id, Long childOrderId, String[] activeNodes) {
        String sql = "SELECT * FROM wf_task WHERE order_id = ?";

        if (childOrderId != null && childOrderId != 0)
            sql += "id NOT IN ( " + childOrderId + " )";

        if (!ObjectUtils.isEmpty(activeNodes)) {
            int i = 0;
            for (String str : activeNodes)
                activeNodes[i++] = "'" + str + "'";

            sql += "AND name IN (" + String.join(",", activeNodes) + ")";
        }

        return CRUD.list(Task.class, sql, id);
    }

    static List<Task> findNextActiveTasks(Long id, String taskName, Long parentTaskId) {
        String sql = "SELECT * FROM wf_task WHERE parent_task_id IN "
                + "( SELECT ht.id FROM wf_task_history ht WHERE ht.order_id = ? AND ht.name = ? AND ht.parent_task_id = ? )";

        return CRUD.list(Task.class, sql, id, taskName, parentTaskId);
    }

    static List<TaskActor> findTaskActorsByTaskId(Long taskId) {
        return CRUD.list(TaskActor.class, "SELECT * FROM wf_task_actor WHERE task_id = ?", taskId);
    }

    static void createTaskActor(Long taskId, Long actorId) {
        String sql = "INSERT INTO wf_task_actor (task_id, actor_id) VALUES (?, ?)";
        CRUD.jdbcWriterFactory().insert(sql, taskId, actorId);
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

//	interface OrderHistoryDao extends IDataService<OrderHistory> {
//		@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
//		public OrderHistory findByOrderId(Long orderId);
//	}
//
//	public static final OrderHistoryDao OrderHistoryDAO = new Caller("cms", "wf_order_history").bind(OrderHistoryDao.class, OrderHistory.class);
}
