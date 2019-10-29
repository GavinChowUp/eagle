

package com.eagle.cloud.gateway.validate.config;

import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * kaptcha 配置
 */
@Configuration
public class KaptchaConfiguration {
    
    @Autowired
    ValidateCodeProperties validateCodeProperties;
    
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "6,60,167");
        properties.put("kaptcha.image.width", validateCodeProperties.getImage().getWidth());
        properties.put("kaptcha.textproducer.font.size", "25");
        properties.put("kaptcha.image.height", validateCodeProperties.getImage().getHeight());
        properties.put("kaptcha.textproducer.char.space", "6");
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        properties.put("kaptcha.textproducer.char.string", "abcde2345678gfynmnpwx");
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        properties.put("kaptcha.textproducer.char.length", String.valueOf(validateCodeProperties.getImage().getLength()));
        properties.put("kaptcha.border.color", "105,179,90");
        properties.put("kaptcha.background.clear.from", "white");
        properties.put("kaptcha.background.clear.to", "white");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
