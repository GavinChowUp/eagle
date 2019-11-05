package com.eagle.cloud.gateway.validate;

import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

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
    void processCode(ServletWebRequest request) throws ValidateCodeException, IOException,
            ServletRequestBindingException;
    
    /**
     * 验证码校验逻辑
     *
     * @param request
     */
    void validataCode(ServletWebRequest request) throws ValidateCodeException, ServletRequestBindingException;
}
