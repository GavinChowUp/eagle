package com.eagle.cloud.gateway.validate;

import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * 验证码处理器，封装不同的验证码处理逻辑
 * 模版方法接口
 *
 * @author: Gavin
 * @Date: 2019/10/10;
 */
public interface ICodeProcessor {
    
    /**
     * 验证码处理逻辑
     * 生成->保存->发送
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> processCode(ServerRequest request) throws ValidateCodeException, IOException;
    
    /**
     * 验证码校验逻辑
     *
     * @param request
     */
    void validataCode(ServerHttpRequest request) throws ValidateCodeException;
}
