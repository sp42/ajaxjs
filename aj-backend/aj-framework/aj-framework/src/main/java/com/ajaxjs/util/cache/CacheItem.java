package com.ajaxjs.util.cache;

import lombok.Data;

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

    public CacheItem(V data, long expire) {
        this.value = data;
        this.expire = expire;
    }
}
