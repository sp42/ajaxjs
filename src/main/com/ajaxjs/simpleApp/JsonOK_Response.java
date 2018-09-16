package com.ajaxjs.simpleApp;

import io.swagger.v3.oas.annotations.media.Schema;

public class JsonOK_Response {
	private boolean isOk;
	private String msg;

	@Schema(title = "操作是否成功", allowableValues = "true, false")
	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	@Schema(title = "反馈的信息，或者异常的信息")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
