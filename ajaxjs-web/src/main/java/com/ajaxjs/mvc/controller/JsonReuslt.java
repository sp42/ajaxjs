package com.ajaxjs.mvc.controller;

/**
 * 
 * @author Frank Cheung
 *
 */
public class JsonReuslt {
	/**
	 * 操作成功，返回 msg 信息，使用 String.format(json_not_ok, "foo", "bar");
	 */
	public static final String json_ok = "{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * 操作失败，返回 msg 信息，使用 String.format(json_not_ok, "foo", "bar");
	 */
	public static final String json_not_ok = "{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 
	 * @param jsonStr 要显示的信息
	 * @param isOk 是否操作成功
	 */
	public JsonReuslt(String jsonStr, boolean isOk) {
		this.isOk = isOk;
		this.setOutputStr(String.format(isOk ? json_ok : json_not_ok, jsonStr));
	}

	public JsonReuslt(String jsonStr) {
		this.setOutputStr(jsonStr);
	}

	/**
	 * 表示操作成功
	 */
	public static final boolean Ok = true;

	/**
	 * 表示操作失敗
	 */
	public static final boolean notOk = false;

	private String outputStr;

	private boolean isOk;

	public String getOutputStr() {
		return outputStr;
	}

	public void setOutputStr(String outputStr) {
		this.outputStr = outputStr;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
}
