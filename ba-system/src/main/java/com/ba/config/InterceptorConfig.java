package com.ba.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * user 拦截配置
 */
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                //拦截的路径
                .addPathPatterns("/**")
                //排查不拦截的路径
                .excludePathPatterns("/**/common/**")
                //注册
                .excludePathPatterns("/**/register/**")
                // 微信
                .excludePathPatterns("/**/webAuthorize/**")
                //登录
                .excludePathPatterns("/**/login/**")
                .excludePathPatterns("/**/login2/**")
                .excludePathPatterns("/**/file/**")
                .excludePathPatterns("/**/sms/**")
                .excludePathPatterns("/**/sysCouponMobUser/useCouponForBox");
    }
}
