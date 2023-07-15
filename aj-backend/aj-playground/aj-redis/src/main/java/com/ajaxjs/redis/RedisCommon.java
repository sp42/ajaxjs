package com.ajaxjs.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisCommon {
    public static LettuceConnectionFactory redisConnectionFactory(String server, int port) {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(server, port));
    }

    public static LettuceConnectionFactory redisConnectionFactory(String server) {
        return redisConnectionFactory(server, 6379);
    }

    public static RedisTemplate<String, Object> initRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(new ObjectMapper()));

        return redisTemplate;
    }
}
