package com.eagle.cloud.gateway.validate.properties;

import lombok.Data;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 */
@Data
public class ImageCodeProperties extends SmsCodeProperties {
    
    private int width = 67;
    private int height = 23;
    
    public ImageCodeProperties() {
        setLength(4);
    }
}
