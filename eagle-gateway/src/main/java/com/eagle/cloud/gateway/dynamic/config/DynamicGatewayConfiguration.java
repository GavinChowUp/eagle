package com.eagle.cloud.gateway.dynamic.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.eagle.cloud.gateway.dynamic.properties.EagleGatewayProp;
import com.eagle.cloud.gateway.dynamic.route.extend.NacosDynRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态路由配置
 *
 * @author Gavin
 * @date 2019/11/5 4:00 下午
 */
@Configuration
@ConditionalOnProperty(
        prefix = "com.eagle.cloud.gateway",
        name = "dynamic.enable",
        havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(EagleGatewayProp.class)
public class DynamicGatewayConfiguration {
    
    @Autowired
    private ZuulProperties zuulProperties;
    
    @Autowired
    private DispatcherServletPath dispatcherServletPath;
    
    @Autowired
    private ApplicationEventPublisher publisher;
    
    @Configuration
    @ConditionalOnProperty(
            prefix = "com.eagle.cloud.gateway",
            name = "dynamic.type",
            havingValue = "nacos",
            matchIfMissing = true)
    public class NacosDynRoute {
        
        @Autowired
        private NacosConfigProperties nacosConfigProperties;
        
        @Autowired
        EagleGatewayProp eagleGatewayProp;
        
        @Bean
        NacosDynRouteLocator nacosDynRouteLocator() {
            return new NacosDynRouteLocator(
                    nacosConfigProperties,
                    eagleGatewayProp,
                    zuulProperties,
                    dispatcherServletPath.getPrefix(),
                    publisher);
        }
    }
}
