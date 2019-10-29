package com.eagle.cloud.gateway.validate.processor;

import com.eagle.cloud.gateway.validate.pojo.ImageCode;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author: Gavin
 * @Date: 2019/10/11
 */
@Component
public class ImageCodeProcessor extends AbstractCodeProcessorAdapter<ImageCode> {
    
    @Override
    protected Mono<ServerResponse> sendCode(ServerRequest request, ImageCode verificationCode) throws IOException {
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(verificationCode.getImage(), "JPEG",os);
    
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }
    
}
