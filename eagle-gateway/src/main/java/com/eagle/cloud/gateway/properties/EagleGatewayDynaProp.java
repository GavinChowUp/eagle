package com.eagle.cloud.gateway.properties;

import lombok.Data;

/**
 * @author Gavin
 * @date 2019/10/24 8:28 下午
 */

@Data
public class EagleGatewayDynaProp {
    
    private boolean enable = true;
    
    private String type = "nacos";
}
