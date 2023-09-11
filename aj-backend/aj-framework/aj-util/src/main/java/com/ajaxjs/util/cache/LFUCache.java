package com.ajaxjs.util.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * LFU 最少使用率策略
 *
 * @param <K>
 * @param <V>
 */
public class LFUCache<K, V> implements Cache<K, V> {
    /**
     * 容量大小
     */
    private final int capacity;

    /**
     * 存储缓存对象
     */
    private final Map<K, CacheObject<V>> cacheMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new HashMap<>(capacity, 1.0f);
    }

    @Override
    public synchronized void put(K key, V value, long timeout) {
        if (isFull())
            pruneCache();

        cacheMap.put(key, new CacheObject<>(value, timeout));
    }

    @Override
    public synchronized V get(K key) {
        CacheObject<V> cacheObject = cacheMap.get(key);

        if (cacheObject == null)
            return null;

        cacheObject.latestTime = System.currentTimeMillis();
        cacheObject.count++;

        return cacheObject.value;
    }

    @Override
    public synchronized void remove(K key) {
        cacheMap.remove(key);
    }

    public boolean isFull() {
        return (capacity > 0) && (cacheMap.size() >= capacity);
    }

    /**
     * 调整缓存
     *
     * @return
     */
    protected int pruneCache() {
        int count = 0;
        CacheObject<V> coMin = null;
        // 清理过期对象并找出访问最少的对象
        Iterator<CacheObject<V>> values = cacheMap.values().iterator();
        CacheObject<V> co;

        while (values.hasNext()) {
            co = values.next();

            if (co.isExpired()) {
                values.remove();
                count++;
                continue;
            }

            // 找出访问最少的对象
            if (coMin == null || co.count < coMin.count)
                coMin = co;
        }

        // 减少所有对象访问量，并清除减少后为0的访问对象
        if (isFull() && coMin != null) {
            long minAccessCount = coMin.count;
            values = cacheMap.values().iterator();
            CacheObject<V> co1;

            while (values.hasNext()) {
                co1 = values.next();
                co1.count -= minAccessCount;

                if (co1.count <= 0) {
                    values.remove();
                    count++;
                }
            }
        }

        return count;
    }
}