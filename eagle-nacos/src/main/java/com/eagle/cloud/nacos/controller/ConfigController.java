package com.eagle.cloud.nacos.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gavin
 * @date 2019/10/23 9:52 上午
 */
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {
    
    
    /**
     * http://localhost:8080/config/get
     */
    @GetMapping("/get")
    public String get() {
        return "useLocalCache";
    }
}
