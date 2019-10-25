package com.eagle.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Gavin
 * @date 2019/10/24 8:22 下午
 */
@Data
@ConfigurationProperties(prefix = "com.eagle.cloud.gateway")
public class EagleGatewayProp {
    
    private EagleNacosProp nacos = new EagleNacosProp();
    
    private EagleGatewayDynaProp dynamic = new EagleGatewayDynaProp();
}
