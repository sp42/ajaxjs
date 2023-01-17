package com.ajaxjs.my_data_framework.enmu_field;

/**
 * 实体状态 这是最通用的状态，供参考使用
 * 
 * @author Frank Cheung
 *
 */
public enum Status implements IntegerValueEnum, DescEnum {
	/**
	 * 已上线/显示/启动/已激活
	 */
	ONLINE(1, "上线"),

	/**
	 * 已下线/不显示/关闭/禁用/不激活
	 */
	OFFLINE(0, "下线"),

	/**
	 * 实体已被删除的标记
	 */
	DELETED(-1, "已删除");

	/**
	 * 
	 * @param value
	 * @param desc
	 */
	Status(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	private Integer value;

	@Override
	public Integer getValue() {
		return value;
	}

	private String desc;

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public boolean isUsingOrdinal() {
		return false;
	}
}