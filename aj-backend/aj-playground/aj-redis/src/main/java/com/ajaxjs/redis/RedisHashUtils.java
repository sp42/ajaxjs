package com.ajaxjs.redis;

import java.util.Map;

public class RedisHashUtils extends RedisUtils {

    /**
     * 添加一个 Map 类型值
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public void hmSet(String key, Map<String, Object> map) {
        getRedisTemplate().opsForHash().putAll(key, map);
    }

    /**
     * 添加一个Map类型值并设置过期时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     */
    public void hmSet(String key, Map<String, Object> map, int time) {
        getRedisTemplate().opsForHash().putAll(key, map);
        expire(key, time);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value
     */
    public void hSet(String key, String item, Object value) {
        getRedisTemplate().opsForHash().put(key, item, value);
    }

    /**
     * 向一张 hash 表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的 hash 表有时间,这里将会替换原有的时间
     */
    public void hSet(String key, String item, Object value, int time) {
        getRedisTemplate().opsForHash().put(key, item, value);
        expire(key, time);
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hDel(String key, Object... item) {
        getRedisTemplate().opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return getRedisTemplate().opsForHash().hasKey(key, item);
    }

}
