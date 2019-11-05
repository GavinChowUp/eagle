package com.eagle.cloud.gateway.validate.sender.impl;


import com.eagle.cloud.gateway.validate.sender.ICodeSender;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
public class DefaultSmsCodeSender implements ICodeSender {
    
    
    @Override
    public void  sendCode(String sendTo, String sendValue) {
        System.out.println("已经向手机号：" + sendTo + " 发送验证码" + sendValue);
    }
}
