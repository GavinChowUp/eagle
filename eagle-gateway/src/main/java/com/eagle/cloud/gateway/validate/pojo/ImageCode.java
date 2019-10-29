package com.eagle.cloud.gateway.validate.pojo;

import lombok.*;

import java.awt.image.BufferedImage;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 * <p>
 * 这里lombok 有个bug，以下4个注解 等于@Data,但是使用@Data 会出现编译错误：
 * Error:(11, 1) java: VerificationCode()可以在com.gavin.security.validate.ValidateCode中访问private
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ImageCode extends VerificationCode {
    
    private BufferedImage image;
    
    public ImageCode(BufferedImage image, String code, int expireTime) {
        super(code, expireTime);
        this.image = image;
    }
}
