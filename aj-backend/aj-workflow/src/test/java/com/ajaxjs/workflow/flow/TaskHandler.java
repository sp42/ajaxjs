package com.ajaxjs.workflow.flow;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.service.handler.DecisionHandler;

public class TaskHandler implements DecisionHandler {
	@Override
	public String decide(Execution exec) {
		return (String) exec.getArgs().get("content");
	}
}