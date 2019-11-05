package com.eagle.cloud.gateway.validate.generater.impl;


import com.eagle.cloud.gateway.validate.generater.ICodeGenerater;
import com.eagle.cloud.gateway.validate.pojo.VerificationCode;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
@Component
public class SmsCodeGenerater implements ICodeGenerater {
    
    @Autowired
    private ValidateCodeProperties validateCodeProperties;
    
    @Override
    public VerificationCode generateCode() {
        String code = RandomStringUtils.randomNumeric(validateCodeProperties.getSms().getLength());
        return new VerificationCode(code, validateCodeProperties.getSms().getExpirationTime());
    }
}
