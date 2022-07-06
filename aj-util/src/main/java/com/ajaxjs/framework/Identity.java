package com.ajaxjs.framework;

/**
 * 使用 id 作为唯一标识
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface Identity<T> {
	public void setId(T id);

	public T getId();

}
