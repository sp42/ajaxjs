package com.ajaxjs.workflow.model.node;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfConstant.TaskType;
import com.ajaxjs.workflow.common.WfData;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;
import com.ajaxjs.workflow.model.TransitionModel;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;
import com.ajaxjs.workflow.model.po.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

/**
 * 结束节点 end 元素
 */
@Slf4j
public class EndModel extends NodeModel {
    private static final long serialVersionUID = -7793175180140842894L;

    @Override
    public void exec(Execution exec) {
        fire(exec1 -> {
            log.info("准备要完成了，运行  End 节点");

            WorkflowEngine engine = exec1.getEngine();
            Order order = exec1.getOrder();
            List<Task> tasks = WfData.findTasksByOrderId(order.getId());// 查找当前活动的任务

            if (!ObjectUtils.isEmpty(tasks))
                for (Task task : tasks) {
                    if (task.getTaskType() == TaskType.MAJOR)
                        throw new WfException("存在未完成的主办任务，请确认！？");

//					engine.taskService.complete(task.getId(), SnakeEngine.AUTO);
                    engine.taskService.complete(task.getId(), null, null);
                }

            engine.orderService.complete(order.getId());// 结束当前流程实例

            // 如果存在父流程，则重新构造 Execution 执行对象，交给父流程的 SubProcessModel 模型 execute
            if (order.getParentId() != null /* || order.getParentId() != 0 */) {
                Order parentOrder = WfData.findOrder(order.getParentId());

                if (parentOrder == null)
                    return;

                ProcessPO process = engine.processService.findById(parentOrder.getProcessId());
                ProcessModel pm = process.getModel();

                if (pm == null)
                    return;

                SubProcessModel spm = (SubProcessModel) pm.getNode(order.getParentNodeName());
                Execution newExecution = new Execution(engine, process, parentOrder, exec1.getArgs());
                newExecution.setChildOrderId(order.getId());
                newExecution.setTask(exec1.getTask());
                spm.execute(newExecution);

                // SubProcessModel 执行结果的 tasks 合并到当前执行对象 execution 的 tasks 列表中
                exec1.addTasks(newExecution.getTasks());
            }
        }, exec);
    }

    /**
     * 结束节点无输出变迁
     */
    @Override
    public List<TransitionModel> getOutputs() {
        return Collections.emptyList();
    }
}
