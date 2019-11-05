package com.eagle.cloud.gateway.validate.filter;


import com.eagle.cloud.core.response.R;
import com.eagle.cloud.gateway.validate.ICodeProcessor;
import com.eagle.cloud.gateway.validate.ValidataCodeProcessorHolder;
import com.eagle.cloud.gateway.validate.constant.ValidateConstant;
import com.eagle.cloud.gateway.validate.exception.ValidateCodeException;
import com.eagle.cloud.gateway.validate.properties.ValidateCodeProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Gavin
 * @Date: 2019/10/9
 * <p>
 * InitializingBean 为bean 提供了属性初始化后的处理方法
 * https://zhuanlan.zhihu.com/p/32096649
 */
@Data
@Component
@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    
    @Autowired
    ValidateCodeProperties validateCodeProperties;
    
    @Autowired
    ValidataCodeProcessorHolder processorHolder;
    
    @Autowired
    ObjectMapper objectMapper;
    
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    
    private Set<String> imageValidateUrls = new HashSet<>();
    private Set<String> smsValidateUrls = new HashSet<>();
    
    
    @Override
    public void afterPropertiesSet() {
        
        imageValidateUrls.add(validateCodeProperties.AUTHENTICATION_FORM);
        smsValidateUrls.add(validateCodeProperties.AUTHENTICATION_MOBILE);
        
        addUrlsToSet(validateCodeProperties.getImage().getInterceptUrls(), imageValidateUrls);
        addUrlsToSet(validateCodeProperties.getSms().getInterceptUrls(), smsValidateUrls);
        
    }
    
    private void addUrlsToSet(String interceptUrls, Set<String> set) {
        
        String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(interceptUrls, ",");
        if (urls != null) {
            for (String url : urls) {
                set.add(url);
            }
        }
    }
    
    
    private String getProcessorType(HttpServletRequest request) {
        
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            
            for (String url : imageValidateUrls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    return ValidateConstant.CODE_TYPE_IMAGE;
                }
            }
            for (String url : smsValidateUrls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    return ValidateConstant.CODE_TYPE_SMS;
                }
            }
        }
        
        return null;
    }
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        
        if (doUrlValidate(request)) {
            String type = getProcessorType(request);
            
            if (type != null) {
                try {
                    ICodeProcessor processor = processorHolder.getProcessor(type);
                    log.info("当前的请求是：" + request.getRequestURI() + " 获取的processor 是" +
                            processor.getClass().getSimpleName() + ",验证码类型是： " + type);
                    
                    processor.validataCode(new ServletWebRequest(request));
                } catch (ValidateCodeException | ServletRequestBindingException e) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(objectMapper.writeValueAsString(R.builder().build().fail(null, e.getMessage())));
                    return;
                }
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(R.builder().build().fail("validate",
                        "该请求路径需校验验证码：" + request.getRequestURI())));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    
    private boolean doUrlValidate(HttpServletRequest request) {
        
        for (String url : imageValidateUrls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        for (String url : smsValidateUrls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }
}
