package com.sanjiang.provider.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Maps;
import com.sanjiang.provider.constrants.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * redis配置
 *
 * @author kimiyu
 * @date 2018/4/26 14:24
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class RedisConfig extends CachingConfigurerSupport {

    private static final Long CACHE_EXPIRES_TIME = 3600L;

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        Map<String, Long> expireMap = Maps.newHashMap();
        expireMap.put(CacheType.SHELF_CACHE, CACHE_EXPIRES_TIME);
        cacheManager.setExpires(expireMap);
        return cacheManager;
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return ((target, method, params) -> {
            StringBuilder sb = new StringBuilder(target.getClass().getName())
                    .append("#")
                    .append(method.getName())
                    .append(":");
            if (params != null && params.length > 0) {
                Arrays.stream(params).filter(Objects::nonNull)
                        .forEach(param -> sb.append(param.toString())
                                .append(","));
            }
            return sb.toString();
        });
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        final StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        template.setKeySerializer(new GenericToStringSerializer<>(Object.class));
        template.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
