package com.ajaxjs.util.cache.expiry;

import com.ajaxjs.util.cache.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可自动移除过期的缓存项
 * <a href="https://www.jianshu.com/p/28d3efa0c1d0">...</a>
 * 比较复杂，<a href="https://blog.csdn.net/jgteng/article/details/56015699">...</a>
 * 原理介绍 延迟阻塞队列 DelayQueue <a href="https://juejin.cn/post/6844903721390833678">...</a>
 */
public class ExpiryCache<K, V> implements Cache<K, V> {
    /**
     * 使用阻塞队列中的等待队列，因为DelayQueue是基于PriorityQueue实现的，而PriorityQueue底层是一个最小堆，可以按过期时间排序，
     * 所以等待队列本身只需要维护根节点的一个定时器就可以了，而且插入和删除都是时间复杂度都是 logN，资源消耗很少
     */
    private final DelayQueue<ExpiryCacheItem<K>> DELAY = new DelayQueue<>();

    /**
     * 键值对集合
     */
    private final Map<K, V> CACHE = new ConcurrentHashMap<>();

    private final AtomicInteger size = new AtomicInteger(0);

    private volatile boolean valid = true;

    public ExpiryCache() {
        // 生成一个线程扫描等待队列的值
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            while (valid) {
                try {
                    // 此方法是阻塞的，没有到过期时间就阻塞在这里，直到取到数据
                    ExpiryCacheItem<K> item = DELAY.take();
                    CACHE.remove(item.getValue());
                    size.decrementAndGet();

//                    System.out.printf("缓存项已过期! key: %s", item.getValue());
                } catch (InterruptedException e) {
                    // ExpiryMap 线程被打断
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 向缓存中添加键值对，并设置过期时间。
     *
     * @param key    键
     * @param data   值
     * @param expire 过期时间，单位为毫秒
     */
    @Override
    public void put(K key, V data, long expire) {
        CACHE.put(key, data);

        // 当等于0时，就不把过期时间放进队列里了，值在代码运行期间会一直存在
        if (expire != 0)
            DELAY.offer(new ExpiryCacheItem<>(key, System.currentTimeMillis() + expire));

        size.incrementAndGet();
//    System.out.printf("添加缓存项。key: %s, value: %s。%n", key, data);
    }

    @Override
    public V get(K key) {
        return CACHE.get(key);
    }

    @Override
    public void remove(K key) {
        CACHE.remove(key);
    }

    /**
     * 清空缓存
     */
    public void clear() {
        size.compareAndSet(size.get(), 0); // 将缓存大小设为 0
        valid = false; // 将 valid 标志设为 false
        DELAY.clear(); // 清空延迟队列
        CACHE.clear(); // 清空缓存
    }

    private static volatile ExpiryCache<String, Object> INSTANCE;

    /**
     * 获取 ExpiryCache 的单例实例
     * 使用单例模式，加上双重验证，可适用于多线程高并发情况
     *
     * @return ExpiryCache 的单例实例
     */
    public static ExpiryCache<String, Object> getInstance() {
        if (INSTANCE == null)
            synchronized (ExpiryCache.class) {
                if (INSTANCE == null)
                    INSTANCE = new ExpiryCache<>();
            }

        return INSTANCE;
    }

}