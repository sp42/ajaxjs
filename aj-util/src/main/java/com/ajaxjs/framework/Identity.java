package com.ajaxjs.framework;

import java.io.Serializable;

/**
 * 使用 id 作为唯一标识
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface Identity {
	default void setId(Serializable id) {
		throw new NullPointerException();
	}

	default Serializable getId() {
		throw new NullPointerException();
	}
}
