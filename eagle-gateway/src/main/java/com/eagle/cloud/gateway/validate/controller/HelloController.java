package com.eagle.cloud.gateway.validate.controller;

import com.eagle.cloud.gateway.validate.ICodeProcessor;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 测试验证码拦截路径
 *
 * @author: Gavin
 * @Date: 2019/11/5
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    
    @Autowired
    ValidateCodeProperties securityProperties;
    
    
    @Autowired
    Map<String, ICodeProcessor> iCodeProcessorMap;
    
    @GetMapping("/1")
    public void helloword(HttpServletResponse response)
            throws IOException {
        
        response.getWriter().write("hello zuul...");
        
    }
    
    @GetMapping
    public void hellowordExtends(HttpServletResponse response)
            throws IOException {
        
        response.getWriter().write("hello zuul...");
        
    }
    
}
