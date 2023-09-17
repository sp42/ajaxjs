package com.ajaxjs.cache.scheduled_cache;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存实现,可自动移除过期的缓存项
 * <a href="https://blog.csdn.net/weixin_34452850/article/details/88016523">...</a>
 */
public class ScheduledCache<K, V> {
    private final DelayQueue<CacheItem<K, V>> cache = new DelayQueue<>();

    private final int capacity;

    private final AtomicInteger size;

    private volatile boolean valid;

    public ScheduledCache(int capacity) {
        this.capacity = capacity;
        size = new AtomicInteger(0);
        valid = true;
        startCheckTask();
    }

    public void put(K key, V value, long timeout) {
        CacheItem<K, V> item = new CacheItem<>();
        item.setKey(key);
        item.setValue(value);
        item.setExpireTimeMillis(System.currentTimeMillis() + timeout);
        cache.put(item);
        size.incrementAndGet();

        System.err.printf("添加缓存项。key: %s, value: %s。%n", item.getKey(), item.getValue());
    }

    public void evict() {
        size.compareAndSet(size.get(), 0);
        valid = false;
        cache.clear();
    }

    private class CheckExpiredItemTask implements Runnable {
        @Override
        public void run() {
            while (valid) {
                try {
                    expire(cache.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startCheckTask() {
        new Thread(new CheckExpiredItemTask()).start();
    }

    private void expire(CacheItem<K, V> item) {
        size.decrementAndGet();
        System.err.printf("缓存项已过期!key: %s, value: %s, 缓存剩余项数量: %s。%n", item.getKey(), item.getValue(), size.get());
    }
}

