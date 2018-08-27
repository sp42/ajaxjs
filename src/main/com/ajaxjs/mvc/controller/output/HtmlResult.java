package com.ajaxjs.mvc.controller.output;

public class HtmlResult extends BaseResult {
	public HtmlResult(String outputStr) {
		super(outputStr);
	}

	public HtmlResult(String outputStr, boolean isOk) {
		super(outputStr, isOk);
	}
}
