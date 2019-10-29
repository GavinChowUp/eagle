package com.eagle.cloud.gateway.validate.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 */
@Data
@ConfigurationProperties(prefix = "com.eagle.cloud.validate")
public class ValidateCodeProperties {
    
    public final String AUTHENTICATION_FORM = "/authentication/form";
    public final String AUTHENTICATION_MOBILE = "/authentication/mobile";
    
    private ImageCodeProperties image = new ImageCodeProperties();
    private SmsCodeProperties sms = new SmsCodeProperties();
    
}
