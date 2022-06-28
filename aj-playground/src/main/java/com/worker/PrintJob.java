package com.worker;

public class PrintJob implements WorkConstant {
	private PrintableObject obj;

	/**
	 * 状态位
	 */
	private Status status;

	/**
	 * 打印
	 */
	public void print() {
	}

	public PrintableObject getObj() {
		return obj;
	}

	public void setObj(PrintableObject obj) {
		this.obj = obj;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
