package com.eagle.cloud.gateway.validate.processor;

import com.eagle.cloud.gateway.validate.ICodeProcessor;
import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import com.eagle.cloud.gateway.validate.generater.ICodeGenerater;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import com.eagle.cloud.gateway.validate.constant.ValidateConstant;
import com.eagle.cloud.gateway.validate.pojo.VerificationCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
public abstract class AbstractCodeProcessorAdapter<T extends VerificationCode> implements ICodeProcessor {
    
    private final String DEFAULT_CODE_KEY = "default_code_key:";
    
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    ValidateCodeProperties validateCodeProperties;
    /**
     * 收集系统中所有的依赖
     * 依赖查找
     */
    @Autowired
    Map<String, ICodeGenerater> iCodeGeneraterMap;
    
    @Override
    public Mono<ServerResponse> processCode(ServerRequest request) throws IOException, ValidateCodeException {
        
        T verificationCode = generater(request);
        saveCode(request, verificationCode);
        return sendCode(request, verificationCode);
        
    }
    
    
    @Override
    public void validataCode(ServerHttpRequest request) throws ValidateCodeException {
        
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor").toLowerCase();
        String randomStr = request.getQueryParams().getFirst("randomStr");
        
        String codeInStore = stringRedisTemplate.opsForValue().get(randomStr);
        
        String codeInRequest = request.getQueryParams().getFirst(type + "Code");
        
        if (StringUtils.isBlank(codeInRequest)) {
            stringRedisTemplate.delete(randomStr);
            throw new ValidateCodeException("验证码的值不能为空");
        }
        
        if (codeInStore == null) {
            throw new ValidateCodeException("验证码不存在/已过期");
        }
        
        if (!StringUtils.equals(codeInStore, codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        
        stringRedisTemplate.delete(randomStr);
    }
    
    
    /**
     * 保存验证码
     *
     * @param request
     * @param verificationCode
     */
    private void saveCode(ServerRequest request, T verificationCode) {
        stringRedisTemplate.opsForValue().set(DEFAULT_CODE_KEY + request.queryParams().getFirst("randomStr"),
                verificationCode.getCode(), validateCodeProperties.getImage().getExpirationTime());
    }
    
    /**
     * 根据请求的方式不同，生成验证码
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private T generater(ServerRequest request) {
        
        String type = getProcessType(request);
        ICodeGenerater codeGenerater = iCodeGeneraterMap.get(type + "CodeGenerater");
        return (T) codeGenerater.generateCode(request);
        
    }
    
    /**
     * 获取请求方式类型
     *
     * @param request
     * @return
     */
    private String getProcessType(ServerRequest request) {
        
        return StringUtils.substringAfter(request.uri().getPath(), ValidateConstant.CODE_TYPE_SEP);
        
    }
    
    /**
     * 发送验证码
     *
     * @param
     * @param verificationCode
     * @return
     */
    protected abstract Mono<ServerResponse> sendCode(ServerRequest request, T verificationCode) throws IOException,
            ValidateCodeException;
    
}
