package com.ajaxjs.mvc.controller.output;

/**
 * JSONP 風格的結果
 * 
 * @author Frank Cheung
 *
 */
public class XmlReuslt extends BaseResult {
	public XmlReuslt(String xmlStr) {
		super(xmlStr);
	}

	public XmlReuslt(String xmlStr, boolean isOk) {
		super(xmlStr, isOk);
	}
}