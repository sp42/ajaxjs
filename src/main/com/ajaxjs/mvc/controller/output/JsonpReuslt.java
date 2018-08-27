package com.ajaxjs.mvc.controller.output;

/**
 * JSONP 風格的結果
 * 
 * @author Frank Cheung
 *
 */
public class JsonpReuslt extends JsonReuslt {
	public JsonpReuslt(String jsonStr) {
		super(jsonStr);
	}

	public JsonpReuslt(String jsonStr, boolean isOk) {
		super(jsonStr, isOk);
	}
}