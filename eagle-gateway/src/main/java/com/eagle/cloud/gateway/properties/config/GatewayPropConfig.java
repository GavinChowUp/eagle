package com.eagle.cloud.gateway.properties.config;

import com.eagle.cloud.gateway.properties.EagleGatewayProp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gavin
 * @date 2019/10/24 8:21 下午
 */
@Configuration
@EnableConfigurationProperties(EagleGatewayProp.class)
public class GatewayPropConfig {
}
