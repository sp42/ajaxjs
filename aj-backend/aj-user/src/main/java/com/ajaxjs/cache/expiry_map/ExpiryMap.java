package com.ajaxjs.cache.expiry_map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * <a href="https://www.jianshu.com/p/28d3efa0c1d0">...</a>
 * 比较复杂，<a href="https://blog.csdn.net/jgteng/article/details/56015699">...</a>
 * 原理介绍 延迟阻塞队列 DelayQueue <a href="https://juejin.cn/post/6844903721390833678">...</a>
 */
public class ExpiryMap {
    private ExpiryMap() {
    }

    private static volatile ExpiryMap expiryMap;

    /**
     * 键值对集合
     */
    private final static Map<String, Object> map = new HashMap<>();

    /**
     * 使用阻塞队列中的等待队列，因为DelayQueue是基于PriorityQueue实现的，而PriorityQueue底层是一个最小堆，可以按过期时间排序，
     * 所以等待队列本身只需要维护根节点的一个定时器就可以了，而且插入和删除都是时间复杂度都是logn，资源消耗很少
     */
    private final static DelayQueue<DelayData<String>> delay = new DelayQueue<>();

    //使用单例模式，加上双重验证，可适用于多线程高并发情况
    public static ExpiryMap getInstance() {
        if (expiryMap == null) {
            synchronized (ExpiryMap.class) {
                if (expiryMap == null) {
                    expiryMap = new ExpiryMap();

                    //生成一个线程扫描等待队列的值
                    Executors.newSingleThreadScheduledExecutor().execute(() -> {
                        while (true) {
                            try {
                                //此方法是阻塞的，没有到过期时间就阻塞在这里，直到取到数据
                                DelayData<String> take = delay.take();
                                map.remove(take.getData());
                            } catch (InterruptedException e) {
                                // ExpiryMap线程被打断
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }

        return expiryMap;
    }

    /**
     * 添加缓存
     *
     * @param key    键
     * @param data   值
     * @param expire 过期时间，单位：毫秒， 0表示无限长
     */
    public void put(String key, Object data, long expire) {
        map.put(key, data);

        // 当等于0时，就不把过期时间放进队列里了，值在代码运行期间会一直存在
        if (expire != 0) delay.offer(new DelayData<>(key, System.currentTimeMillis() + expire));
    }

    /**
     * 读取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return map.get(key);
    }

    /**
     * 清除缓存
     *
     * @param key 键
     * @return 值
     */
    public Object remove(String key) {
        return map.remove(key);
    }

    /**
     * 查询当前缓存的键值对数量
     *
     * @return 数量
     */
    public int size() {
        return map.size();
    }
}