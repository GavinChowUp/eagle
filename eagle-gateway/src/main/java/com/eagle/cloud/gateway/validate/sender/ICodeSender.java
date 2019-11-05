package com.eagle.cloud.gateway.validate.sender;

/**
 * 验证码发送接口
 *
 * @author: Gavin
 * @Date: 2019/10/11
 */
public interface ICodeSender {
    
   void sendCode(String sendTo, String sendValue);
}
