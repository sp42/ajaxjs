package com.ajaxjs.framework;

/**
 * 枚举值是 int 的
 * 
 * @author Frank Cheung<sp42@qq.com>
 */
public interface IntegerValueEnum extends BaseEnmu {
	/**
	 * 获取 POJO 字段的值
	 * 
	 * @return
	 */
	public Integer getValue();

	/**
	 * 说明是否使用自动的序号 value。
	 * 
	 * 为了省事，不用给枚举赋值 1、2、3……，使用系统的索引。但是有个问题 ordinal 是 0 开始的，这里标记是 ordinal，那么反序列化的时候
	 * +1。 当然如果你自己定义 value，那么就要把这个值设为 false
	 * 
	 * @return
	 */
	default boolean isUsingOrdinal() {
		return true;
	}
}
