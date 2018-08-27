package com.ajaxjs.mvc.controller.output;

/**
 * 
 * @author Frank Cheung
 *
 */
public abstract class BaseResult {
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

	private String outputStr;

	private boolean isOk;

	/**
	 * 
	 * @param jsonStr 要显示的信息
	 * @param isOk 是否操作成功
	 */
	public BaseResult(String outputStr, boolean isOk) {
		this.setOk(isOk);
		this.setOutputStr(String.format(isOk ? json_ok : json_not_ok, outputStr));
	}

	public BaseResult(String outputStr) {
		this.setOutputStr(outputStr);
	}

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