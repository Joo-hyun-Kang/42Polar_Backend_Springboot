package com._polar._polar_backend_spring.v1.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginProducer {
    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic loginQueueTopic;

    public void addJob(String accessToken) {
        redisTemplate.convertAndSend(loginQueueTopic.getTopic(), accessToken);
    }
}

