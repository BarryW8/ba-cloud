package com.ba.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器处理
 */
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            //取出当前请求的header，找到jwt令牌
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    // 将header向下传递
                    log.info("feign interceptor,获取request值向下传递,headerName={} and headerValue={}", headerName, headerValue);
                    requestTemplate.header(headerName, headerValue);
                }
            }
            Enumeration<String> bodyNames = request.getParameterNames();
            if (bodyNames != null) {
                while (bodyNames.hasMoreElements()) {
                    String name = bodyNames.nextElement();
                    String values = request.getParameter(name);
                    log.info("feign interceptor,获取request值向下传递,requestName={} and requestValue={}", name, values);
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}
