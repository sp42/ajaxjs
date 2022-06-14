package com.ajaxjs.framework.worker;

/**
 * 打印机
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Printer {
	private String No;
	private String ip;
	private Integer port;
	public String getNo() {
		return No;
	}
	public void setNo(String no) {
		No = no;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
}
