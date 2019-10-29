package com.eagle.cloud.gateway.validate.properties;

import lombok.Data;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 */
@Data
public class SmsCodeProperties {
    
    
    private int length = 6;
    private int expirationTime = 300;
    
    private String interceptUrls;
}
