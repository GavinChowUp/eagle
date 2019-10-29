package com.eagle.cloud.gateway.validate.sender.impl;


import com.eagle.cloud.gateway.validate.sender.ICodeSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
public class DefaultSmsCodeSender implements ICodeSender {
    
    
    @Override
    public Mono<ServerResponse> sendCode(String sendTo, String sendValue) {
        System.out.println("已经向手机号：" + sendTo + " 发送验证码" + sendValue);
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject("验证码发送成功"));
    }
}
