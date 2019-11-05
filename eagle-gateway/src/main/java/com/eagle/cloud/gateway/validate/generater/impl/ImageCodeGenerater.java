package com.eagle.cloud.gateway.validate.generater.impl;


import com.eagle.cloud.gateway.validate.generater.ICodeGenerater;
import com.eagle.cloud.gateway.validate.pojo.ImageCode;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import com.google.code.kaptcha.Producer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.awt.image.BufferedImage;

/**
 * @author: Gavin
 * @Date: 2019/10/10
 */
@Data
public class ImageCodeGenerater implements ICodeGenerater {
    
    @Autowired
    private  Producer producer;
    @Autowired
    private  RedisTemplate redisTemplate;
    @Autowired
    private ValidateCodeProperties validateCodeProperties;
    
    
    @Override
    public ImageCode generateCode() {
        
        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        
        return new ImageCode(image, text, 60);
    }
}
