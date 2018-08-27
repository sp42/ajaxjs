package com.ajaxjs.mvc.controller;

/**
 * 
 * @author Frank Cheung
 *
 */
public class JSONReuslt {
	/**
	 * 表示操作成功
	 */
	public static final boolean Ok = true;

	/**
	 * 表示操作失敗
	 */
	public static final boolean notOk = false;

	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "{\"isOk\": false, \"msg\" : \"%s\"}";

	private String jsonStr;

	private boolean isOk;

	/**
	 * 
	 * @param jsonStr 要显示的信息
	 * @param isOk 是否操作成功
	 */
	public JSONReuslt(String jsonStr, boolean isOk) {
		this.setOk(isOk);
		this.setJsonStr(String.format(isOk ? json_ok : json_not_ok, jsonStr));
	}

	public JSONReuslt(String jsonStr) {
		this.setJsonStr(jsonStr);
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
}
