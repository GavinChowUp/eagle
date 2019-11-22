package com.eagle.cloud.gateway.dynamic.route.extend;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.eagle.cloud.gateway.dynamic.pojo.ZuulRouteEntity;
import com.eagle.cloud.gateway.dynamic.properties.EagleGatewayProp;
import com.eagle.cloud.gateway.dynamic.route.AbstractDynRouteLocator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Gavin
 * @date 2019/11/5 4:57 下午
 */
@Slf4j
public class NacosDynRouteLocator extends AbstractDynRouteLocator {
    
    private NacosConfigProperties nacosConfigProperties;
    
    private EagleGatewayProp eagleGatewayProp;
    
    private ApplicationEventPublisher publisher;
    
    private NacosDynRouteLocator locator;
    
    @Setter
    private List<ZuulRouteEntity> zuulRouteEntities;
    
    
    public NacosDynRouteLocator(NacosConfigProperties nacosConfigProperties,
                                EagleGatewayProp eagleGatewayProp,
                                ZuulProperties zuulProperties,
                                String path,
                                ApplicationEventPublisher publisher) {
        super(path, zuulProperties);
        this.nacosConfigProperties = nacosConfigProperties;
        this.eagleGatewayProp = eagleGatewayProp;
        this.publisher = publisher;
        this.locator = this;
        addListener();
    }
    
    private void addListener() {
        try {
            nacosConfigProperties.configServiceInstance().addListener(
                    eagleGatewayProp.getNacos().getListeningDateID(),
                    eagleGatewayProp.getNacos().getListeningGroup(), new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return null;
                        }
                        
                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            //赋值路由信息
                            locator.setZuulRouteEntities(getListByStr(configInfo));
                            RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(locator);
                            publisher.publishEvent(routesRefreshedEvent);
                        }
                    });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }
    

    
    @Override
    public Map<String, ZuulProperties.ZuulRoute> loadDynamicRoute() {
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        if (zuulRouteEntities == null) {
            zuulRouteEntities = getNacosConfig();
        }
        for (ZuulRouteEntity result : zuulRouteEntities) {
            if (StrUtil.isBlank(result.getPath()) || !result.isEnabled()) {
                continue;
            }
            ZuulRoute zuulRoute = new ZuulRoute();
            BeanUtil.copyProperties(result, zuulRoute);
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }
    
    private List<ZuulRouteEntity> getNacosConfig() {
        try {
            String content = nacosConfigProperties.configServiceInstance().getConfig(
                    eagleGatewayProp.getNacos().getListeningDateID(),
                    eagleGatewayProp.getNacos().getListeningGroup(),5000);
            return getListByStr(content);
        } catch (NacosException e) {
            log.error("listenerNacos-error", e);
        }
        return new ArrayList<>(0);
    }
    
    private List<ZuulRouteEntity> getListByStr(String configInfo) {
        if (StrUtil.isNotEmpty(configInfo)) {
            return JSONObject.parseArray(configInfo, ZuulRouteEntity.class);
        }
        return new ArrayList<>(0);
    }
}
