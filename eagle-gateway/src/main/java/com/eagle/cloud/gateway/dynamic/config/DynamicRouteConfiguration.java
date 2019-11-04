package com.eagle.cloud.gateway.dynamic.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.eagle.cloud.gateway.dynamic.NacosRouteDefinitionRepository;
import com.eagle.cloud.gateway.dynamic.properties.EagleGatewayProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gavin
 * @date 2019/10/24 4:23 下午
 */
@Configuration
@ConditionalOnProperty(prefix = "com.eagle.cloud", name = "gateway.dynamic.enable", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(EagleGatewayProp.class)
public class DynamicRouteConfiguration {
    
    @Autowired
    private ApplicationEventPublisher publisher;
    
    @Configuration
    @ConditionalOnProperty(prefix = "com.eagle.cloud", name = "gateway.dynamic.type", havingValue = "nacos",
            matchIfMissing = true)
    public class NacosDynRoute {
        
        @Autowired
        private NacosConfigProperties nacosConfigProperties;
        
        @Autowired
        EagleGatewayProp eagleGatewayProp;
        
        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
            return new NacosRouteDefinitionRepository(publisher, nacosConfigProperties, eagleGatewayProp);
        }
    }
}
