package com.ajaxjs.spring.response;

import java.nio.charset.StandardCharsets;

public class ResponseResult {
	private Integer status;

	private Integer total;

	private String errorCode;

	private String message;

	private String data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private final static String ERR = "{\"status\": %s, \"errorCode\": \"%s\", \"message\": \"%s\"}";

	private final static String OK = "{\"status\": %s, \"message\": \"%s\", \"data\": %s}";

	private final static String OK_PAGE = "{\"status\": %s, \"message\": \"%s\", \"total\": %s, \"data\": %s}";

	/**
	 * 字符串原文输出。在返回的字符串前面加上这个字符串即可识别。
	 */
	public static final String PLAIN_TEXT_OUTPUT = "PLAIN_TEXT_OUTPUT";

	@Override
	public String toString() {
		String str = "";

		if (errorCode != null)
			str = String.format(ERR, "0", errorCode, message == null ? "未知异常" : message);
		else if (data != null) {

			if (total != null)
				str = String.format(OK_PAGE, "1", message == null ? "分页数据" : message, total, data);
			else
				str = String.format(OK, "1", message == null ? "操作成功" : message, data);
		} else
			str = String.format(OK, "1", "null", message == null ? "操作成功" : message);

		return str;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 方便 Spring 转换器使用
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		return toString().getBytes(StandardCharsets.UTF_8);
	}
}
