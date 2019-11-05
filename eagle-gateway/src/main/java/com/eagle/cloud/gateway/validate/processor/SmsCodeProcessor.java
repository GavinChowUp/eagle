package com.eagle.cloud.gateway.validate.processor;


import com.eagle.cloud.gateway.validate.pojo.VerificationCode;
import com.eagle.cloud.gateway.validate.sender.ICodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
@Component
public class SmsCodeProcessor extends AbstractCodeProcessorAdapter<VerificationCode> {
    
    @Autowired
    ICodeSender smsCodeSender;
    
    @Override
    protected void sendCode(ServletWebRequest request,
                            VerificationCode verificationCode) throws ServletRequestBindingException {
    
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(),"mobile");
    
        smsCodeSender.sendCode(mobile, verificationCode.getCode());
    }
}
