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
    
    private final String SUCCESS_CODE = "000000";
    
    private String code;
    
    private String msg;
    
    private Date date;
    
    private Object resultBody;
    
    public R ok(Object resultBody) {
        this.code = SUCCESS_CODE;
        this.resultBody = resultBody;
        this.date = new Date();
        return this;
    }
    
    public R fail(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.date = new Date();
        return this;
    }
    
    public R fail(String code) {
        this.code = code;
        this.date = new Date();
        return this;
    }
    
}
