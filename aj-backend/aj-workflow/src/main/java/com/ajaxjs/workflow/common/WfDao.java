//package com.ajaxjs.workflow.common;
//
//import java.util.List;
//
//import com.ajaxjs.workflow.model.po.Order;
//import com.ajaxjs.workflow.model.po.OrderCc;
//import com.ajaxjs.workflow.model.po.OrderHistory;
//import com.ajaxjs.workflow.model.po.ProcessPO;
//import com.ajaxjs.workflow.model.po.Surrogate;
//import com.ajaxjs.workflow.model.po.Task;
//import com.ajaxjs.workflow.model.po.TaskActor;
//import com.ajaxjs.workflow.model.po.TaskHistory;
//
///**
// * DAO
// *
// * @author Frank Cheung sp42@qq.com
// *
// */
//public interface WfDao {
//	interface ProcessDao extends IDataService<ProcessPO> {
//		@Select("SELECT max(version) FROM ${tableName} WHERE name = ?")
//		@KeyOfMapParams("name")
//		Integer getLatestProcessVersion(String name);
//	}
//
//	public static final ProcessDao ProcessDAO = new Caller("cms", "wf_process").bind(ProcessDao.class, ProcessPO.class);
//
//	interface TaskDao extends IDataService<Task> {
//		@Select("SELECT * FROM ${tableName} WHERE parentTaskId IN "
//				+ "( SELECT ht.id FROM wf_hist_task ht WHERE ht.order_id = ? AND ht.task_name = ? AND ht.parent_task_id = ? )")
//		public List<Task> getNextActiveTasks(Long id, String taskName, Long parentTaskId);
//
//		@KeyOfMapParams({ "task_id", "actor_id" })
//		Object createTaskActor(Long taskId, Long actorId);
//
////		@Select("SELECT * FROM wf_task_actor WHERE taskId = ?")
//		@KeyOfMapParams("taskId")
//		List<TaskActor> findTaskActorsByTaskId(Long taskId);
//	}
//
//	public static final TaskDao TaskDAO = new Caller("cms", "wf_task").bind(TaskDao.class, Task.class);
//
//
//	interface OrderHistoryDao extends IDataService<OrderHistory> {
//		@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
//		public OrderHistory findByOrderId(Long orderId);
//	}
//
//	public static final OrderHistoryDao OrderHistoryDAO = new Caller("cms", "wf_order_history").bind(OrderHistoryDao.class, OrderHistory.class);
//

//}
