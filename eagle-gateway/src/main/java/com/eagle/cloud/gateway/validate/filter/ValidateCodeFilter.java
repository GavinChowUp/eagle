package com.eagle.cloud.gateway.validate.filter;


import com.eagle.cloud.core.response.R;
import com.eagle.cloud.gateway.validate.ICodeProcessor;
import com.eagle.cloud.gateway.validate.ValidataCodeProcessorHolder;
import com.eagle.cloud.gateway.validate.constant.ValidateConstant;
import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 * <p>
 * InitializingBean 为bean 提供了属性初始化后的处理方法
 * https://zhuanlan.zhihu.com/p/32096649
 */
@Data
@Component
@Slf4j
public class ValidateCodeFilter extends AbstractGatewayFilterFactory implements InitializingBean {
    
    @Autowired
    ValidateCodeProperties validateCodeProperties;
    
    @Autowired
    ValidataCodeProcessorHolder processorHolder;
    
    @Autowired
    ObjectMapper objectMapper;
    
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    
    private Map<String, String> validateUrlsWithType = new HashMap<>();
    
    
    @Override
    public void afterPropertiesSet() {
        
        validateUrlsWithType.put(validateCodeProperties.AUTHENTICATION_FORM, ValidateConstant.CODE_TYPE_IMAGE);
        addUrlsToMap(validateCodeProperties.getImage().getInterceptUrls(), ValidateConstant.CODE_TYPE_IMAGE);
        
        validateUrlsWithType.put(validateCodeProperties.AUTHENTICATION_MOBILE, ValidateConstant.CODE_TYPE_SMS);
        addUrlsToMap(validateCodeProperties.getSms().getInterceptUrls(), ValidateConstant.CODE_TYPE_SMS);
        
        
    }
    
    private void addUrlsToMap(String interceptUrls, String codeType) {
        
        String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(interceptUrls, ",");
        if (urls != null) {
            for (String url : urls) {
                validateUrlsWithType.put(url, codeType);
            }
        }
    }
    
    
    private String getProcessorType(ServerHttpRequest request) {
        
        if (StringUtils.equalsIgnoreCase(request.getMethodValue(), "post")) {
            Set<String> urls = validateUrlsWithType.keySet();
            
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getURI().getPath())) {
                    return validateUrlsWithType.get(url);
                }
            }
        }
        
        return null;
    }
    
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            String type = getProcessorType(request);
            
            if (type != null) {
                try {
                    ICodeProcessor processor = processorHolder.getProcessor(type);
                    log.info("当前的请求是：" + request.getURI().getPath() + " 获取的processor 是" +
                            processor.getClass().getSimpleName() + ",验证码类型是： " + type);
                    
                    processor.validataCode(request);
                } catch (ValidateCodeException e) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                    try {
                        return response.writeWith(Mono.just(
                                response.bufferFactory().wrap(objectMapper.writeValueAsBytes(
                                        R.builder().build().fail("验证码校验失败", e.getMessage())))));
                    } catch (JsonProcessingException e1) {
                        e1.printStackTrace();
                    }
                    return response.setComplete();
                }
            }
            
            return chain.filter(exchange);
        };
        
    }
}
