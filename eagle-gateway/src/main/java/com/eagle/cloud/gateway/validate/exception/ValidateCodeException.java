package com.eagle.cloud.gateway.validate.exception;


/**
 * @author: Gavin
 * @Date: 2019/10/9
 */
public class ValidateCodeException extends Exception {
    
    private static final long serialVersionUID = -4012857367508394443L;
    
    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }
    
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
