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
     * 子流程执行的处理
     */
    @Override
    public void handle(Execution exec) {
        // 根据子流程模型名称获取子流程定义对象
        WorkflowEngine engine = exec.getEngine();
        ProcessPO process = engine.processService.findByVersion(model.getProcessName(), model.getVersion());
        Execution child = new Execution(exec, process, model.getName());
        Order order;

        if (isFutureRunning) {
            // 创建单个线程执行器来执行启动子流程的任务
            ExecutorService es = Executors.newSingleThreadExecutor();
            // 提交执行任务，并返回 future
            Future<Order> future = es.submit(new ExecuteTask(exec, process, model.getName()));

            try {
                es.shutdown();
                order = future.get();
            } catch (InterruptedException e) {
                throw new WfException("创建子流程线程被强制终止执行", e.getCause());
            } catch (ExecutionException e) {
                throw new WfException("创建子流程线程执行异常.", e.getCause());
            }
        } else
            order = engine.startInstanceByExecution(child);

        Objects.requireNonNull(order, "子流程创建失败");
        exec.addTasks(WfData.findTasksByOrderId(order.getId()));
    }
}
