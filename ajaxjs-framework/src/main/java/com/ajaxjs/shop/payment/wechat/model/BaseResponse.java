package com.ajaxjs.shop.payment.wechat.model;

/**
 * 响应内容基类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class BaseResponse {

	/**
	 * 返回状态码
	 */
	private String return_code;

	/**
	 * 返回信息
	 */
	private String return_msg;

	/**
	 * 结果代码
	 */
	private String result_code;

	/**
	 * 错误代码
	 */
	private String error_code;

	/**
	 * 错误描述
	 */
	private String error_code_desc;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_code_desc() {
		return error_code_desc;
	}

	public void setError_code_desc(String error_code_desc) {
		this.error_code_desc = error_code_desc;
	}

	/**
	 * 统一下单响应是否成功
	 * 
	 * @return true 表示成功
	 */
	public boolean isSuccess() {
		return "SUCCESS".equals(return_code) && "SUCCESS".equals(result_code);
	}

}
