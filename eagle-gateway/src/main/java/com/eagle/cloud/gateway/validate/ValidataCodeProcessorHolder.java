package com.eagle.cloud.gateway.validate;

import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: Gavin
 * @Date: 2019/10/14
 */

@Component
public class ValidataCodeProcessorHolder {
    
    @Autowired
    Map<String, ICodeProcessor> iCodeProcessors;
    
    public ICodeProcessor getProcessor(String type) throws ValidateCodeException {
        
        String name = type + "CodeProcessor";
        ICodeProcessor processor = iCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("不支持该验证码校验类型");
        }
        return processor;
    }
}
