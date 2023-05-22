package com.ajaxjs.workflow.flow;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.service.handler.IHandler;

public class CustomHandler implements IHandler {
	@Override
	public void handle(Execution exec) {
		System.out.println("custom handler");
	}
}