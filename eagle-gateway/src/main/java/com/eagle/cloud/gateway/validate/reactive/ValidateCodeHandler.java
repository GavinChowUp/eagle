package com.eagle.cloud.gateway.validate.reactive;

import com.eagle.cloud.gateway.validate.ICodeProcessor;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author Gavin
 * @date 2019/10/29 5:10 下午
 */
@Slf4j
@Configuration
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {
    
    @Autowired
    ValidateCodeProperties validateCodeProperties;
    
    
    @Autowired
    Map<String, ICodeProcessor> iCodeProcessorMap ;
    
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        String type = StringUtils.substringAfter(request.uri().getPath(), "/code/");
        ICodeProcessor iCodeProcessor = iCodeProcessorMap.get(type + "CodeProcessor");
        try {
            return iCodeProcessor.processCode(request);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
