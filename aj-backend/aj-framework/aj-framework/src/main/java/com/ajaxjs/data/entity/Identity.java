package com.ajaxjs.data.entity;

/**
 * 使用 id 作为唯一标识
 */
public interface Identity<T> {
    default void setId(T id) {

    }

    default T getId() {
        return null;
    }
}
