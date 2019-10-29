package com.eagle.cloud.gateway.validate.config;


import com.eagle.cloud.gateway.validate.generater.ICodeGenerater;
import com.eagle.cloud.gateway.validate.generater.impl.ImageCodeGenerater;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import com.eagle.cloud.gateway.validate.sender.ICodeSender;
import com.eagle.cloud.gateway.validate.sender.impl.DefaultSmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 为什么在ImageCodeGenerater 这边没有设置@Component注解呢？
 * 目的值为了在下方使用@ConditionalOnMissingBean 注解
 *
 * @author: Gavin
 * @Date: 2019/10/10
 */
@Configuration
@EnableConfigurationProperties(ValidateCodeProperties.class)
public class ValidataCodeBeanConfig {
    
    @Autowired
    private ValidateCodeProperties validateCodeProperties;
    
    /**
     * 不存在 imageCodeGenerater 这样的Bean 的时候，使用以下配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerater")
    public ICodeGenerater imageCodeGenerater() {
        ImageCodeGenerater imageCodeGenerater = new ImageCodeGenerater();
        imageCodeGenerater.setValidateCodeProperties(validateCodeProperties);
        return imageCodeGenerater;
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "smsCodeSender")
    public ICodeSender smsCodeSender() {
        
        return new DefaultSmsCodeSender();
    }
}
