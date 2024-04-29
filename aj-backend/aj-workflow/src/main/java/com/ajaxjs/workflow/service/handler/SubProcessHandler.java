package com.ajaxjs.workflow.service.handler;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.common.WfData;
import com.ajaxjs.workflow.common.WfException;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.node.work.SubProcessModel;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 启动子流程的处理器
 */
public class SubProcessHandler implements IHandler {
    private final SubProcessModel model;

    /**
     * 是否以 future 方式执行启动子流程任务
     */
    private boolean isFutureRunning = false;

    public SubProcessHandler(SubProcessModel model) {
        this.model = model;
    }

    public SubProcessHandler(SubProcessModel model, boolean isFutureRunning) {
        this.model = model;
        this.isFutureRunning = isFutureRunning;
    }

    /**
     * 处理子流程的执行逻辑。
     *
     * @param exec 表示当前执行流程的对象，用于获取流程引擎和其他相关信息并进行操作。
     */
    @Override
    public void handle(Execution exec) {
        // 根据子流程模型名称获取子流程定义对象
        WorkflowEngine engine = exec.getEngine();
        ProcessPO process = engine.processService.findByVersion(model.getProcessName(), model.getVersion());
        Execution child = new Execution(exec, process, model.getName());
        Order order;

        if (isFutureRunning) {
            // 使用单线程执行器异步执行启动子流程的任务
            ExecutorService es = Executors.newSingleThreadExecutor();
            // 提交执行任务，并返回 future
            Future<Order> future = es.submit(new ExecuteTask(exec, process, model.getName()));

            try {
                es.shutdown(); // 关闭执行器
                order = future.get(); // 等待任务完成并获取结果
            } catch (InterruptedException e) {
                // 处理线程被中断的异常
                throw new WfException("创建子流程线程被强制终止执行", e.getCause());
            } catch (ExecutionException e) {
                // 处理执行任务时产生的异常
                throw new WfException("创建子流程线程执行异常.", e.getCause());
            }
        } else
            // 否则，直接同步方式启动子流程
            order = engine.startInstanceByExecution(child);

        // 验证子流程创建是否成功
        Objects.requireNonNull(order, "子流程创建失败");
        // 将子流程中的任务添加到当前执行流程的任务列表中
        exec.addTasks(WfData.findTasksByOrderId(order.getId()));
    }

}
