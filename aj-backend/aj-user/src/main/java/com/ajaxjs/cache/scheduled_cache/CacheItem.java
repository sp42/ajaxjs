package com.ajaxjs.cache.scheduled_cache;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 缓存项
 */
@Getter
@Setter
public class CacheItem<K, V> implements Delayed {
    private K key;

    private V value;

    private long expireTimeMillis;

    @Override
    public long getDelay(TimeUnit unit) {
        return expireTimeMillis - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        CacheItem<K, V> item = (CacheItem<K, V>) o;

        return (expireTimeMillis > item.expireTimeMillis ? 1 : 0);
    }
}
