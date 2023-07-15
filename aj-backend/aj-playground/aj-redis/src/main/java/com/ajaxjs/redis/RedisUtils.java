package com.ajaxjs.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RedisUtils {
    private RedisTemplate<String, Object> redisTemplate;

    RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        Object o = get(key);

        return o == null ? null : o.toString();
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Integer getInt(String key) {
        String value = getString(key);

        return value == null ? null : Integer.parseInt(value);
    }

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 添加缓存并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time 要大于0 如果 time 小于等于0 将设置无限期
     */
    public void set(String key, Object value, long time) {
        if (time > 0)
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        else
            set(key, value);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return true 存在 false 不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除 key
     *
     * @param key KEY
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public void expire(String key, int time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key 键 不能为 null
     * @return 时间(秒) 返回 0 代表为永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (!ObjectUtils.isEmpty(key)) {
            if (key.length == 1)
                redisTemplate.delete(key[0]);
            else
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
        }
    }

    /**
     * 递增
     *
     * @param key 键
     */
    public Long increment(String key, long delta) {
        if (delta < 0)
            throw new RuntimeException("递增因子必须大于0");

        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key 键
     */
    public Long decrement(String key, long delta) {
        if (delta < 0)
            throw new RuntimeException("递减因子必须大于0");

        return redisTemplate.opsForValue().increment(key, -delta);
    }
}
