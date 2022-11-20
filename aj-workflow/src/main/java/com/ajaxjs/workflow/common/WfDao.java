package com.ajaxjs.workflow.common;

import java.util.List;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.data_service.sdk.KeyOfMapParams;
import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.workflow.model.po.CCOrderPO;
import com.ajaxjs.workflow.model.po.OrderHistory;
import com.ajaxjs.workflow.model.po.OrderPO;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Surrogate;
import com.ajaxjs.workflow.model.po.TaskActor;
import com.ajaxjs.workflow.model.po.TaskHistoryPO;
import com.ajaxjs.workflow.model.po.TaskPO;

/**
 * DAO
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface WfDao {
	interface ProcessDao extends IDataService<ProcessPO> {
		@Select("SELECT max(version) FROM ${tableName} WHERE name = ?")
		@KeyOfMapParams("name")
		Integer getLatestProcessVersion(String name);
	}

	public static final ProcessDao ProcessDAO = new Caller("wf", "process").bind(ProcessDao.class, ProcessPO.class);

	interface TaskDao extends IDataService<TaskPO> {
		@Select("SELECT * FROM ${tableName} WHERE parentTaskId IN "
				+ "( SELECT ht.id FROM wf_hist_task ht WHERE ht.order_id = ? AND ht.task_name = ? AND ht.parent_task_id = ? )")
		public List<TaskPO> getNextActiveTasks(Long id, String taskName, Long parentTaskId);
		
		@Insert("INSERT INTO wf_task_actor (taskId, actorId) VALUES (?, ?)")
		public int createTaskActor(Long taskId, Long actorId);
		
		@Select("SELECT * FROM wf_task_actor WHERE taskId = ?")
		public List<TaskActor> findTaskActorsByTaskId(Long taskId);
	}

	public static final TaskDao TaskDAO = new Caller("wf", "task").bind(TaskDao.class, TaskPO.class);

	interface TaskHistoryDao extends IDataService<TaskHistoryPO> {
	}

	public static final TaskHistoryDao TaskHistoryDAO = new Caller("wf", "task_history").bind(TaskHistoryDao.class, TaskHistoryPO .class);

	interface OrderDao extends IDataService<OrderPO> {
	}

	public static final OrderDao OrderDAO = new Caller("wf", "order").bind(OrderDao.class, OrderPO.class);

	interface OrderHistoryDao extends IDataService<OrderHistory> {
		@Select("SELECT * FROM ${tableName} WHERE orderId = ?")
		public OrderHistory findByOrderId(Long orderId);
	}

	public static final OrderHistoryDao OrderHistoryDAO = new Caller("wf", "order_history").bind(OrderHistoryDao.class, OrderHistory.class);

	interface OrderCcDao extends IDataService<CCOrderPO> {
	}

	public static final OrderCcDao OrderCcDAO = new Caller("wf", "order_cc").bind(OrderCcDao.class, CCOrderPO.class);
	
	interface SurrogateDao extends IDataService<Surrogate> {
	}
	
	public static final SurrogateDao SurrogateDAO = new Caller("wf", "surrogate").bind(SurrogateDao.class, Surrogate.class);
}
