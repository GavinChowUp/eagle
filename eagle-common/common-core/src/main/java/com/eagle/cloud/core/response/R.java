package com.eagle.cloud.core.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 统一返回
 *
 * @author: Gavin
 * @Date: 2019/10/9
 */
@Data
@Builder
public class R {
    
    
    private String code;
    
    private String msg;
    
    private Date date;
    
    private Object resultBody;
    
    public R ok(Object resultBody) {
        this.code = "request_success";
        this.resultBody = resultBody;
        this.date = new Date();
        return this;
    }
    
    public R fail(String code, String msg) {
        this.code = "request_fail:" + code;
        this.msg = msg;
        this.date = new Date();
        return this;
    }
    
    public R fail(String code) {
        this.code = "request_fail:" + code;
        this.date = new Date();
        return this;
    }
    
}
