package com.ba.config;

import com.ba.base.UserContext;
import com.ba.interceptor.AuthorityInterceptor;
import com.ba.interceptor.CommonInterceptor;
import com.ba.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * user 拦截配置
 */
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private CommonInterceptor commonInterceptor;

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private AuthorityInterceptor authorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> passPaths = new ArrayList<>();
        passPaths.add("/**/common/**");
        passPaths.add("/**/register/**"); // 注册
        passPaths.add("/**/login/**"); // 登录
        passPaths.add("/**/file/**"); // 文件
        passPaths.add("/**/sms/**"); // 短信
        passPaths.add("/**/webAuthorize/**"); // 微信

        // 通用拦截器
        registry.addInterceptor(commonInterceptor)
                // 拦截的路径
                .addPathPatterns("/**");

        // 登录拦截器
        registry.addInterceptor(loginInterceptor)
                // 拦截的路径
                .addPathPatterns("/**")
                // 放行的路径
                .excludePathPatterns(passPaths);

        // 权限校验拦截器
        registry.addInterceptor(authorityInterceptor)
                // 拦截的路径
                .addPathPatterns("/**")
                // 放行的路径
                .excludePathPatterns(passPaths);
    }

}
