package com._polar._polar_backend_spring.v1.redis;

import com._polar._polar_backend_spring.v1.redis.annotataion.LoginRedisTemplate;
import com._polar._polar_backend_spring.v1.redis.annotataion.MailRedisTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class V1RedisConfig {
    @Bean
    @LoginRedisTemplate
    public RedisTemplate<String, String> loginRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // キーと値のシリアライザーを明示的に設定
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    @MailRedisTemplate
    public RedisTemplate<String, Object> mailRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // JSONでObjectを保存

        return template;
    }
}
