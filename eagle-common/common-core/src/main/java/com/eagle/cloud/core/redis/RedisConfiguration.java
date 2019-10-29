package com.eagle.cloud.core.redis;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Gavin
 * @date 2019/10/28 5:21 下午
 */
@Configuration
@EnableCaching
@AllArgsConstructor
@EnableConfigurationProperties(EagleRedisProperties.class)
public class RedisConfiguration extends CachingConfigurerSupport {
    private final RedisConnectionFactory factory;
    
    @Autowired
    EagleRedisProperties redisProperties;
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        /**
         * 生成一个默认配置，通过config对象即可对缓存进行自定义配置
         */
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.entryTtl(Duration.ofMinutes(1));
        
        /**
         * 自定义的缓存配置
         */
        Map<String, Integer> cacheMap = redisProperties.getCacheMap();
        Set<String> cacheNames = cacheMap.keySet();
        
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        for (String cacheName : cacheNames) {
            configMap.put(cacheName, config.entryTtl(Duration.ofSeconds(cacheMap.get(cacheName))));
        }
        
        /**
         * 使用自定义的缓存配置初始化一个cacheManager
         * 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
         */
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }
    
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }
    
    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }
    
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }
    
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }
    
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
