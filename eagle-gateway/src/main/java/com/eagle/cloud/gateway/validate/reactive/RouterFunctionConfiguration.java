

package com.eagle.cloud.gateway.validate.reactive;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


/**
 * 路由配置信息
 * <p>
 * 需呀注意，reactive 不能提高浏览器的响应时间，但是大大提高了后端的可伸缩性
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
    
    @Autowired
    ValidateCodeHandler validateCodeHandler;
    
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.nest(
                RequestPredicates.path("/code"),
                RouterFunctions.route(RequestPredicates.GET("/{type}").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        validateCodeHandler::handle)
        );
    }
    
}
