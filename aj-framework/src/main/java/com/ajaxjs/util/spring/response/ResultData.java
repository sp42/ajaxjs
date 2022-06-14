package com.ajaxjs.util.spring.response;

public class ResultData<T> {
	/** 结果状态 ,具体状态码参见ResultData.java */
	private Integer status;
	private String message;
	private T data;

	private long timestamp;

	public static <T> ResultData<T> ok(T data) {
		ResultData<T> resultData = new ResultData<>();
		resultData.setStatus(ReturnCode.RC100.getCode());
		resultData.setMessage(ReturnCode.RC100.getMessage());
		resultData.setData(data);
		
		return resultData;
	}

	public static <T> ResultData<T> fail(int code, String message) {
		ResultData<T> resultData = new ResultData<>();
		resultData.setStatus(code);
		resultData.setMessage(message);
		
		return resultData;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
