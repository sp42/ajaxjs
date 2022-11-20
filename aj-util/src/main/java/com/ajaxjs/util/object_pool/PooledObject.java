package com.ajaxjs.util.object_pool;

public class PooledObject<T> {
	private T objection = null;// 外界使用的对象

	private boolean busy = false; // 此对象是否正在使用的标志，默认没有正在使用

	/**
	 * 构造函数，池化对象
	 * 
	 * @param objection
	 */
	public PooledObject(T objection) {
		this.objection = objection;
	}

	/**
	 * 返回此对象中的对象
	 * 
	 * @return
	 */
	public T getObject() {
		return objection;
	}

	/**
	 * 设置此对象的，对象
	 * 
	 * @param objection
	 */
	public void setObject(T objection) {
		this.objection = objection;
	}

	/**
	 * 获得对象对象是否忙
	 * 
	 * @return
	 */
	public boolean isBusy() {
		return busy;
	}

	/**
	 * 设置对象的对象正在忙
	 * 
	 * @param busy
	 */
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
}