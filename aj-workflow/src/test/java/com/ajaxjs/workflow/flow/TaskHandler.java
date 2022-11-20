package com.ajaxjs.workflow.flow;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.service.handler.DecisionHandler;

public class TaskHandler implements DecisionHandler {
	@Override
	public String decide(Execution execution) {
		return (String) execution.getArgs().get("content");
	}
}