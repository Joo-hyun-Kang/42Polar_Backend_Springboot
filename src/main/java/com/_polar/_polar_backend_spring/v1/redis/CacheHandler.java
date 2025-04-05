package com._polar._polar_backend_spring.v1.redis;

import com._polar._polar_backend_spring.v1.redis.annotataion.MailRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheHandler {
    private final RedisTemplate<String, Object> redisTemplate;

    public CacheHandler(@MailRedisTemplate RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object getCacheOrNull(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Boolean deleteCache(String key) {
        return redisTemplate.delete(key);
    }
}
