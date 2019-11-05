package com.eagle.cloud.gateway.validate.processor;

import com.eagle.cloud.gateway.validate.pojo.ImageCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
@Component
public class ImageCodeProcessor extends AbstractCodeProcessorAdapter<ImageCode> {
    
    @Override
    protected void sendCode(ServletWebRequest request, ImageCode verificationCode) throws IOException {
        ImageIO.write(verificationCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
    
}
