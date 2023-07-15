package com.ajaxjs.redis;

import java.util.Set;

public class RedisSetUtils extends RedisUtils {
    /**
     * 根据key获取 Set 中的所有值
     *
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        return getRedisTemplate().opsForSet().members(key);
    }

    /**
     * 根据 value 从一个 set 中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false 不存在
     */
    public Boolean sHasKey(String key, Object value) {
        return getRedisTemplate().opsForSet().isMember(key, value);
    }

    /**
     * 将数据放入 set 缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        return getRedisTemplate().opsForSet().add(key, values);
    }

    /**
     * 将 set 数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key, int time, Object... values) {
        Long count = getRedisTemplate().opsForSet().add(key, values);
        if (time > 0) expire(key, time);

        return count;
    }

    /**
     * 获取 set 缓存的长度
     *
     * @param key 键
     */
    public Long sGetSetSize(String key) {
        return getRedisTemplate().opsForSet().size(key);
    }

    /**
     * 移除值为 value 的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        return getRedisTemplate().opsForSet().remove(key, values);
    }
}
