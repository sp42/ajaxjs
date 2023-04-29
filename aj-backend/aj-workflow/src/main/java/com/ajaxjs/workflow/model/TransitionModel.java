package com.ajaxjs.workflow.model;

import java.util.List;
import java.util.Map;

import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.workflow.model.node.NodeModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.node.work.TaskModel;
import com.ajaxjs.workflow.model.po.Task;
import com.ajaxjs.workflow.service.TaskService;
import com.ajaxjs.workflow.service.handler.IHandler;
import com.ajaxjs.workflow.service.handler.SubProcessHandler;
import com.ajaxjs.workflow.service.interceptor.WorkflowInterceptor;
import lombok.Data;

/**
 * 变迁定义 transition 元素
 */
@Data
public class TransitionModel extends BaseWfModel {
	public static final LogHelper LOGGER = LogHelper.getLog(TransitionModel.class);

	private static final long serialVersionUID = 3688123410411321158L;

	/**
	 * 变迁的源节点引用
	 */
	private NodeModel source;

	/**
	 * 变迁的目标节点引用
	 */
	private NodeModel target;

	/**
	 * 变迁的目标节点 name 名称
	 */
	private String to;

	/**
	 * 变迁的条件表达式，用于 decision
	 */
	private String expr;

	/**
	 * 转折点图形数据
	 */
	private String g;

	/**
	 * 描述便宜位置
	 */
	private String offset;

	/**
	 * 当前变迁路径是否可用
	 */
	private boolean enabled = false;

	public void execute(Execution exec) {
		if (!enabled)
			return;

		if (target instanceof TaskModel) {
			final TaskModel tm = (TaskModel) target;

			// 如果目标节点模型为 TaskModel，则创建 task，这是 CreateTaskHandler
			fire(new IHandler() {
				@Override
				public void handle(Execution exec) {
					LOGGER.info("创建 {0} 任务", tm.getName());

					List<Task> tasks = TaskService.createTaskByModel(tm, exec);
					exec.addTasks(tasks);

					// 从服务上下文中查找任务拦截器列表，依次对 task 集合进行拦截处理
					Map<String, WorkflowInterceptor> interceptors = DiContextUtil.findByInterface(WorkflowInterceptor.class);

					try {
						for (String id : interceptors.keySet())
							interceptors.get(id).intercept(exec);
					} catch (Exception e) {
						LOGGER.warning("拦截器执行失败=" + e.getMessage());
					}
				}
			}, exec);
		} else if (target instanceof SubProcessModel)
			fire(new SubProcessHandler((SubProcessModel) target), exec);// 如果目标节点模型为 SubProcessModel，则启动子流程
		else
			target.execute(exec);// 如果目标节点模型为其它控制类型，则继续由目标节点执行
	}
}
