package com.ajaxjs.monitor.model;

/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */

/**
 * JVM 信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
public class JvmInfo {
	/**
	 * jdk版本
	 */
	String jdkVersion;

	/**
	 * jdk Home
	 */
	String jdkHome;

	/**
	 * jak name
	 */
	private String jdkName;

	/**
	 * JVM 厂商
	 */
	private String vendor;

	/**
	 * 系统架构
	 */
	private String arch;

	/**
	 * 总内存
	 */
	String jvmTotalMemory;

	/**
	 * Java虚拟机将尝试使用的最大内存量
	 */
	String maxMemory;

	/**
	 * 空闲内存
	 */
	String freeMemory;

	/**
	 * 已使用内存
	 */
	String usedMemory;

	/**
	 * 内存使用率
	 */
	private double usePercent;

	/**
	 * 返回Java虚拟机的启动时间（毫秒）。此方法返回Java虚拟机启动的大致时间。
	 */
	private long startTime;

	/**
	 * 返回Java虚拟机的正常运行时间（毫秒）
	 */
	private long uptime;

	public String getJdkVersion() {
		return jdkVersion;
	}

	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}

	public String getJdkHome() {
		return jdkHome;
	}

	public void setJdkHome(String jdkHome) {
		this.jdkHome = jdkHome;
	}

	public String getJdkName() {
		return jdkName;
	}

	public void setJdkName(String jdkName) {
		this.jdkName = jdkName;
	}

	public String getJvmTotalMemory() {
		return jvmTotalMemory;
	}

	public void setJvmTotalMemory(String jvmTotalMemory) {
		this.jvmTotalMemory = jvmTotalMemory;
	}

	public String getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}

	public String getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	public String getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}

	public double getUsePercent() {
		return usePercent;
	}

	public void setUsePercent(double usePercent) {
		this.usePercent = usePercent;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

}
