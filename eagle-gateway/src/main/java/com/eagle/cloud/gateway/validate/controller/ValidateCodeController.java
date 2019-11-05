package com.eagle.cloud.gateway.validate.controller;

import com.eagle.cloud.gateway.validate.ICodeProcessor;
import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 */
@RestController
@RequestMapping("/code")
public class ValidateCodeController {
    
    @Autowired
    ValidateCodeProperties securityProperties;
    
    
    @Autowired
    Map<String, ICodeProcessor> iCodeProcessorMap;
    
    @GetMapping("/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletRequestBindingException, ValidateCodeException {
        
        String type = StringUtils.substringAfter(request.getRequestURI(), "/code/");
        ICodeProcessor iCodeProcessor = iCodeProcessorMap.get(type + "CodeProcessor");
        iCodeProcessor.processCode(new ServletWebRequest(request, response));
        
    }
    
}
