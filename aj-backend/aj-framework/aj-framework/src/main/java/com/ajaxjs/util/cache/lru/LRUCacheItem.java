package com.ajaxjs.util.cache.lru;

public class LRUCacheItem<V> {
    /**
     * 缓存对象
     */
    V value;

    /**
     * 最新访问时间
     * 用于判断对象是否过期，惰性删除的时候使用
     */
    long latestTime;

    /**
     * 访问次数
     * 用于 LFU 判断
     */
    int count;

    /**
     * 对象存活时长，0表示永久存活
     * 用于判断对象是否过期，惰性删除的时候使用
     */
    private final long ttl;

    /**
     * 构造
     *
     * @param value 值
     * @param ttl   超时时长
     */
    public LRUCacheItem(V value, long ttl) {
        this.value = value;
        this.ttl = ttl;
        this.latestTime = System.currentTimeMillis();
    }

    /**
     * 存活到期对象的判断
     *
     * @return true 表示过期
     */
    public boolean isExpired() {
        /* 删除访问次数最少的对象时，将其他对象的访问数减去这个最小访问数，否则老的数据越来越大，对新进入对象不公平，随着时间的增长，还有可能溢出 */
        return latestTime + ttl < System.currentTimeMillis();
    }
}