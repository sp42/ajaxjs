package com.ajaxjs.workflow.service;

import com.ajaxjs.workflow.WorkflowEngine;
import com.ajaxjs.workflow.model.Args;

public class StartProcess implements Runnable {
    private final WorkflowEngine engine;
    private final Long processId;

    public StartProcess(WorkflowEngine engine, Long processId) {
        this.engine = engine;
        this.processId = processId;
    }

    @Override
    public void run() {
        Args args = new Args();
        args.put("task1.operator", new String[]{"1"});

        try {
            engine.startInstanceById(processId, 2L, args);// simple 流程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
