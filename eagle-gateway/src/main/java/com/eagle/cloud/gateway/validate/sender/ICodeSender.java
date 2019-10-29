package com.eagle.cloud.gateway.validate.sender;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 验证码发送接口
 *
 * @author: Gavin
 * @Date: 2019/10/11
 */
public interface ICodeSender {
    
    Mono<ServerResponse> sendCode(String sendTo, String sendValue);
}
