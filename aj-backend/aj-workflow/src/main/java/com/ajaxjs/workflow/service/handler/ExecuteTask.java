package com.ajaxjs.workflow.service.handler;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.po.Order;
import com.ajaxjs.workflow.model.po.ProcessPO;

import java.util.concurrent.Callable;

/**
 * Future 模式的任务执行。通过 call 返回任务结果集
 */
public class ExecuteTask implements Callable<Order> {
    private final WorkflowEngine engine;

    private final Execution child;

    /**
     * 构造函数
     */
    public ExecuteTask(Execution exec, ProcessPO process, String parentNodeName) {
        engine = exec.getEngine();
        child = new Execution(exec, process, parentNodeName);
    }

    @Override
    public Order call() {
        return engine.startInstanceByExecution(child);
    }
}
