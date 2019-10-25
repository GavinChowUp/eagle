package com.eagle.cloud.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Gavin
 * @date 2019/10/22 8:53 下午
 */
@SpringBootApplication(scanBasePackages = "com.eagle.cloud")
@EnableDiscoveryClient
public class NacosApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NacosApplication.class, args);
    }
    
}

    
