package com.ajaxjs.workflow.flow;

import com.ajaxjs.workflow.model.DecisionHandler;
import com.ajaxjs.workflow.model.Execution;

public class TaskHandler implements DecisionHandler {
	@Override
	public String decide(Execution execution) {
		return (String) execution.getArgs().get("content");
	}
}