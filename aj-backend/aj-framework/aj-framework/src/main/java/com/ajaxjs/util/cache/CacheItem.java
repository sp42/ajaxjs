package com.ajaxjs.util.cache;

import lombok.Data;

/**
 * 被缓存的数据
 *
 * @param <V> 缓存数据的类型
 */
@Data
public class CacheItem<V> {
    /**
     * 缓存值
     */
    private V value;

    /**
     * 到期时间
     */
    private long expire;

    /**
     * 创建一个 CacheItem
     *
     * @param data   缓存值
     * @param expire 到期时间
     */
    public CacheItem(V data, long expire) {
        this.value = data;
        this.expire = expire;
    }
}
