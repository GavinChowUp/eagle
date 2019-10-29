package com.eagle.cloud.core.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Gavin
 * @date 2019/10/28 5:49 下午
 */
@Data
@ConfigurationProperties(prefix = "com.eagle.cloud.redis")
public class EagleRedisProperties {
    
    private Map<String, Integer> cacheMap;
}
