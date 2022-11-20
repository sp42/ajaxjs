package com.ajaxjs.workflow.model.work;

import com.ajaxjs.workflow.model.Execution;
import com.ajaxjs.workflow.model.ProcessModel;

/**
 * 子流程定义 subprocess 元素
 * 
 * 
 */
public class SubProcessModel extends WorkModel {
	private static final long serialVersionUID = -3923955459202018147L;

	/**
	 * 子流程名称
	 */
	private String processName;

	/**
	 * 子流程版本号
	 */
	private Integer version;

	/**
	 * 子流程定义引用
	 */
	private ProcessModel subProcess;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}

	public ProcessModel getSubProcess() {
		return subProcess;
	}

	public void setSubProcess(ProcessModel subProcess) {
		this.subProcess = subProcess;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
