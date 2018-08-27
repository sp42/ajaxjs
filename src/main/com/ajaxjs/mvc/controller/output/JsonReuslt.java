package com.ajaxjs.mvc.controller.output;

/**
 * 
 * @author Frank Cheung
 *
 */
public class JsonReuslt extends BaseResult {
	/**
	 * 操作成功，返回 msg 信息
	 */
	public static final String json_ok = "{\"isOk\": true, \"msg\" : \"%s\"}";

	/**
	 * 操作失败，返回 msg 信息
	 */
	public static final String json_not_ok = "{\"isOk\": false, \"msg\" : \"%s\"}";

	/**
	 * 
	 * @param jsonStr 要显示的信息
	 * @param isOk 是否操作成功
	 */
	public JsonReuslt(String jsonStr, boolean isOk) {
		super(String.format(isOk ? json_ok : json_not_ok, jsonStr), isOk);
	}

	public JsonReuslt(String jsonStr) {
		super(jsonStr);
	}
}
