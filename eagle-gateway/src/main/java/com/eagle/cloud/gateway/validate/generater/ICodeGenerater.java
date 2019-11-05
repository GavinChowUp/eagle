package com.eagle.cloud.gateway.validate.generater;

import com.eagle.cloud.gateway.validate.pojo.VerificationCode;

/**
 * 验证码生成器接口
 *
 * @author: Gavin
 * @Date: 2019/10/10
 */
public interface ICodeGenerater {
    
    VerificationCode generateCode();
}
