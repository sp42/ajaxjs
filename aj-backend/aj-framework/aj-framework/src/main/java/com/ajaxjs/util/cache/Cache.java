package com.ajaxjs.util.cache;

/**
 * 缓存接口
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public interface Cache<K, V> {
    /**
     * 将对象加入到缓存
     *
     * @param key     键
     * @param value   对象
     * @param timeout 过期时间
     */
    void put(K key, V value, long timeout);

    /**
     * 从缓存中获得对象
     *
     * @param key 键
     * @return 键对应的对象
     */
    V get(K key);

    /**
     * 从缓存中删除对象
     *
     * @param key 键
     */
    void remove(K key);
}
