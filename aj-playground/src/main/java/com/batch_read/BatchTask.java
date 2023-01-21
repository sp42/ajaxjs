package com.batch_read;

public abstract class BatchTask {
	public final static String TXT_IMP_EXP = "101";
	public final static String EXCEL_IMP_EXP = "102";
	public final static String TASK_RUNNING = "2";
	public final static String TASK_FINISHED = "4";
	public final static String TASK_FAILED = "5";
	
	protected BatchDTO taskContext = null;

	public BatchTask(BatchDTO taskContext) {
		this.taskContext = taskContext;
	}

	public abstract void doBatch() throws Exception;
}