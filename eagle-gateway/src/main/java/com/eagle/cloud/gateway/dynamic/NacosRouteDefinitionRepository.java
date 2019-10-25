package com.eagle.cloud.gateway.dynamic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.eagle.cloud.gateway.properties.EagleGatewayProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 动态路由数据加载类
 *
 * @author Gavin
 * @date 2019/10/24 9:40 上午
 */
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
    
    private final String EAGLE_DATA_ID;
    
    private final String EAGLE_GROUP_ID;
    
    private ApplicationEventPublisher publisher;
    
    private NacosConfigProperties nacosConfigProperties;
    
    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher,
                                          NacosConfigProperties configProperties, EagleGatewayProp eagleGatewayProp) {
        this.publisher = publisher;
        this.nacosConfigProperties = configProperties;
        
        EAGLE_DATA_ID = eagleGatewayProp.getNacos().getListeningDateID();
        EAGLE_GROUP_ID = eagleGatewayProp.getNacos().getListeningGroup();
        addListener();
    }
    
    /**
     * 添加Nacos监听
     */
    private void addListener() {
        try {
            nacosConfigProperties.configServiceInstance()
                    .addListener(EAGLE_DATA_ID, EAGLE_GROUP_ID, new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return null;
                        }
                        
                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            publisher.publishEvent(new RefreshRoutesEvent(this));
                        }
                    });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }
    
    /**
     * 重写方法，实现路由信息读取
     *
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        
        try {
            log.info("【eagle gateway】start refresh routeDefinition...");
            String content = nacosConfigProperties.configServiceInstance()
                    .getConfig(EAGLE_DATA_ID, EAGLE_GROUP_ID, 5000);
            
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            log.info("【eagle gateway】success refresh routeDefinition...");
            return Flux.fromIterable(routeDefinitions);
            
        } catch (NacosException e) {
            log.error("【eagle gateway】 getRouteDefinitions by nacos error", e);
        }
        return Flux.fromIterable(CollUtil.newArrayList());
    }
    
    private List<RouteDefinition> getListByStr(String content) {
        if (StrUtil.isNotEmpty(content)) {
            return JSONObject.parseArray(content, RouteDefinition.class);
        }
        return new ArrayList<>(0);
    }
    
    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }
    
    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
