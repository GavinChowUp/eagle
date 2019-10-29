package com.eagle.cloud.gateway.validate.processor;


import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import com.eagle.cloud.gateway.validate.pojo.VerificationCode;
import com.eagle.cloud.gateway.validate.sender.ICodeSender;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
@Component
public class SmsCodeProcessor extends AbstractCodeProcessorAdapter<VerificationCode> {
    
    @Autowired
    ICodeSender smsCodeSender;
    
    @Override
    protected Mono<ServerResponse> sendCode(ServerRequest request,
                                            VerificationCode verificationCode) throws ValidateCodeException {
        
        String mobile = request.queryParams().getFirst("mobile");
        if (StringUtils.isBlank(mobile)) {
            throw new ValidateCodeException("手机号不能为空");
        }
        
        return smsCodeSender.sendCode(mobile, verificationCode.getCode());
    }
}
