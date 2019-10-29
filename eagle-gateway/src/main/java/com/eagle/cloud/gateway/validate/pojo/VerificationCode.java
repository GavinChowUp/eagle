package com.eagle.cloud.gateway.validate.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: Gavin
 * @Date: 2019/10/10
 */
@Data
public class VerificationCode {
    
    private String code;
    
    private LocalDateTime expireTime;
    
    public VerificationCode(String code, int expireTime) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }
}
