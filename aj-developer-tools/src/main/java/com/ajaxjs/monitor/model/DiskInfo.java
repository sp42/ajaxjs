/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.ajaxjs.monitor.model;

/**
 * 磁盘信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */

public class DiskInfo {
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 文件系统的卷名
	 */
	private String volume;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 文件系统的逻辑卷名
	 */
	private String logicalVolume;

	/**
	 * 文件系统的挂载点
	 */
	private String mount;

	/**
	 * 文件系统的描述
	 */
	private String description;

	/**
	 * 文件系统的选项
	 */
	private String options;

	/**
	 * 文件系统的类型（FAT、NTFS、etx2、ext4等）
	 */
	private String type;

	/**
	 * UUID/GUID
	 */
	private String UUID;

	/**
	 * 分区大小
	 */
	private String size;
	private Long totalSpace;

	/**
	 * 已使用
	 */
	private String used;
	
	private Long usableSpace;

	/**
	 * 可用
	 */
	private String avail;

	/**
	 * 已使用百分比
	 */
	private double usePercent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLogicalVolume() {
		return logicalVolume;
	}

	public void setLogicalVolume(String logicalVolume) {
		this.logicalVolume = logicalVolume;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(Long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public Long getUsableSpace() {
		return usableSpace;
	}

	public void setUsableSpace(Long usableSpace) {
		this.usableSpace = usableSpace;
	}

	public String getAvail() {
		return avail;
	}

	public void setAvail(String avail) {
		this.avail = avail;
	}

	public double getUsePercent() {
		return usePercent;
	}

	public void setUsePercent(double usePercent) {
		this.usePercent = usePercent;
	}

}
